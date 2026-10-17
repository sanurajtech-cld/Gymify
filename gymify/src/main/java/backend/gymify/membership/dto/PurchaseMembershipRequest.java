package backend.gymify.membership.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PurchaseMembershipRequest(
        @NotNull(message = "Plan ID is required")
        UUID planId,

        LocalDate startDate,

        UUID trainerId
) {}
