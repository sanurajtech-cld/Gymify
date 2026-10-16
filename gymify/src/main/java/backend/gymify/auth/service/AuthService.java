package backend.gymify.auth.service;

import backend.gymify.auth.dto.*;
import backend.gymify.auth.entity.PasswordResetToken;
import backend.gymify.auth.entity.RefreshToken;
import backend.gymify.auth.repository.PasswordResetTokenRepository;
import backend.gymify.auth.repository.RefreshTokenRepository;
import backend.gymify.common.exception.BadRequestException;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.common.exception.UnauthorizedException;
import backend.gymify.common.security.JwtUtil;
import backend.gymify.user.entity.Role;
import backend.gymify.user.entity.Role.RoleName;
import backend.gymify.user.entity.User;
import backend.gymify.user.repository.RoleRepository;
import backend.gymify.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered");
        }

        if (request.phone() != null && userRepository.existsByPhone(request.phone())) {
            throw new BadRequestException("Phone number already registered");
        }

        RoleName roleName;
        try {
            roleName = RoleName.valueOf(request.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.role());
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setRoles(Set.of(role));

        user = userRepository.save(user);

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);

        return generateAuthResponse(user);
    }

    @Transactional
    public void logout(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public void requestPasswordReset(PasswordResetRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiresAt(Instant.now().plusSeconds(3600)); // 1 hour
            passwordResetTokenRepository.save(token);
            // TODO: Send email with reset link
        });
    }

    @Transactional
    public void confirmPasswordReset(PasswordResetConfirm request) {
        PasswordResetToken token = passwordResetTokenRepository.findByTokenAndIsUsedFalse(request.token())
                .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        if (!token.isValid()) {
            throw new BadRequestException("Token has expired");
        }

        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        token.setIsUsed(true);
        passwordResetTokenRepository.save(token);

        refreshTokenRepository.deleteByUserId(user.getId());
    }

    private AuthResponse generateAuthResponse(User user) {
        String primaryRole = user.getRoles().stream()
                .map(r -> r.getName().name())
                .findFirst()
                .orElse("CUSTOMER");

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), primaryRole);
        String refreshTokenStr = jwtUtil.generateRefreshToken(user.getId());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenStr);
        refreshToken.setExpiresAt(Instant.now().plusMillis(jwtUtil.getRefreshTokenExpiration()));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream().map(r -> r.getName().name()).toList(),
                accessToken,
                refreshTokenStr
        );
    }
}
