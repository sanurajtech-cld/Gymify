package backend.gymify.membership.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMembershipPlanRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be positive")
        Integer durationDays,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        String currency,

        boolean includesTrainer,

        Integer maxFreezeDays
) {}
