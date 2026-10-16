package backend.gymify.membership.dto;

import backend.gymify.membership.entity.Membership;

import java.time.LocalDate;
import java.util.UUID;

public record MembershipResponse(
        UUID id,
        UUID userId,
        UUID gymId,
        String gymName,
        UUID planId,
        String planName,
        UUID assignedTrainerId,
        String assignedTrainerName,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        LocalDate freezeStartDate,
        LocalDate freezeEndDate,
        int totalFreezeDaysUsed
) {
    public static MembershipResponse from(Membership membership) {
        return new MembershipResponse(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGym().getId(),
                membership.getGym().getName(),
                membership.getPlan().getId(),
                membership.getPlan().getName(),
                membership.getAssignedTrainer() != null ? membership.getAssignedTrainer().getId() : null,
                membership.getAssignedTrainer() != null ?
                        membership.getAssignedTrainer().getFirstName() + " " + membership.getAssignedTrainer().getLastName() : null,
                membership.getStartDate(),
                membership.getEndDate(),
                membership.getStatus().name(),
                membership.getFreezeStartDate(),
                membership.getFreezeEndDate(),
                membership.getTotalFreezeDaysUsed()
        );
    }
}
