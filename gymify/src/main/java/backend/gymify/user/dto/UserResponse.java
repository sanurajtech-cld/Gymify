package backend.gymify.user.dto;

import backend.gymify.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String phone,
        String firstName,
        String lastName,
        String profileImageUrl,
        LocalDate dateOfBirth,
        String gender,
        List<String> roles,
        boolean isActive,
        boolean isEmailVerified
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImageUrl(),
                user.getDateOfBirth(),
                user.getGender() != null ? user.getGender().name() : null,
                user.getRoles().stream().map(r -> r.getName().name()).toList(),
                user.getIsActive(),
                user.getIsEmailVerified()
        );
    }
}
