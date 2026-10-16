package backend.gymify.workout.dto;

import backend.gymify.workout.entity.WorkoutAssignment;
import java.time.LocalDate;
import java.util.UUID;

public record WorkoutAssignmentResponse(
        UUID id, UUID planId, String planName, UUID customerId, String customerName,
        UUID trainerId, String trainerName, UUID gymId, LocalDate startDate,
        LocalDate endDate, String status, String notes
) {
    public static WorkoutAssignmentResponse from(WorkoutAssignment a) {
        return new WorkoutAssignmentResponse(a.getId(), a.getPlan().getId(), a.getPlan().getName(),
                a.getCustomer().getId(), a.getCustomer().getFirstName() + " " + a.getCustomer().getLastName(),
                a.getTrainer().getId(), a.getTrainer().getFirstName(),
                a.getGym() != null ? a.getGym().getId() : null,
                a.getStartDate(), a.getEndDate(), a.getStatus().name(), a.getNotes());
    }
}
