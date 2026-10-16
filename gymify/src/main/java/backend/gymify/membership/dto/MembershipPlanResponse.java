package backend.gymify.membership.dto;

import backend.gymify.membership.entity.MembershipPlan;

import java.math.BigDecimal;
import java.util.UUID;

public record MembershipPlanResponse(
        UUID id,
        UUID gymId,
        String name,
        String description,
        int durationDays,
        BigDecimal price,
        String currency,
        boolean includesTrainer,
        int maxFreezeDays,
        boolean isActive
) {
    public static MembershipPlanResponse from(MembershipPlan plan) {
        return new MembershipPlanResponse(
                plan.getId(),
                plan.getGym().getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getDurationDays(),
                plan.getPrice(),
                plan.getCurrency(),
                plan.getIncludesTrainer(),
                plan.getMaxFreezeDays(),
                plan.getIsActive()
        );
    }
}
