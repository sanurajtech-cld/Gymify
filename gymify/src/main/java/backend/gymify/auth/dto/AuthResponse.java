package backend.gymify.auth.dto;

import java.util.List;
import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        List<String> roles,
        String accessToken,
        String refreshToken
) {}
