package backend.gymify.user.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProfileRequest(
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Size(max = 20)
        String phone,

        LocalDate dateOfBirth,

        String gender,

        @Size(max = 500)
        String profileImageUrl
) {}
