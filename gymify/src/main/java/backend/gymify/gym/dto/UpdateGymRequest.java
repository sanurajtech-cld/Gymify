package backend.gymify.gym.dto;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record UpdateGymRequest(
        @Size(max = 200)
        String name,

        String description,

        String logoUrl,

        String addressLine1,

        String addressLine2,

        String city,

        String state,

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
