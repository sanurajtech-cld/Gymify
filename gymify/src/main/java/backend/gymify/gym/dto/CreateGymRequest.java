package backend.gymify.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateGymRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 200)
        String name,

        String description,

        String logoUrl,

        @NotBlank(message = "Address is required")
        String addressLine1,

        String addressLine2,

        @NotBlank(message = "City is required")
        String city,

        String state,

        @NotBlank(message = "Country is required")
        String country,

        String postalCode,

        Double latitude,

        Double longitude,

        String phone,

        String email,

        String website,

        List<UUID> facilityIds,

        List<OperatingHoursRequest> operatingHours
) {}
