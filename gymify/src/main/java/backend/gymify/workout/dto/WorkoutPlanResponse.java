package backend.gymify.workout.dto;

import backend.gymify.workout.entity.WorkoutPlan;
import java.util.List;
import java.util.UUID;

public record WorkoutPlanResponse(
        UUID id, UUID trainerId, String trainerName, String name, String description,
        String goal, String difficulty, Integer durationWeeks, boolean isTemplate,
        boolean isPublished, List<WorkoutDayResponse> days
) {
    public static WorkoutPlanResponse from(WorkoutPlan plan) {
        return new WorkoutPlanResponse(plan.getId(), plan.getTrainer().getId(),
                plan.getTrainer().getFirstName() + " " + plan.getTrainer().getLastName(),
                plan.getName(), plan.getDescription(), plan.getGoal(),
                plan.getDifficulty() != null ? plan.getDifficulty().name() : null,
                plan.getDurationWeeks(), plan.getIsTemplate(), plan.getIsPublished(),
                plan.getWorkoutDays().stream().map(WorkoutDayResponse::from).toList());
    }

    public static WorkoutPlanResponse summary(WorkoutPlan plan) {
        return new WorkoutPlanResponse(plan.getId(), plan.getTrainer().getId(),
                plan.getTrainer().getFirstName(), plan.getName(), plan.getDescription(),
                plan.getGoal(), plan.getDifficulty() != null ? plan.getDifficulty().name() : null,
                plan.getDurationWeeks(), plan.getIsTemplate(), plan.getIsPublished(), null);
    }
}
