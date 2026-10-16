package backend.gymify.workout.dto;

import backend.gymify.workout.entity.WorkoutExercise;
import java.math.BigDecimal;
import java.util.UUID;

public record WorkoutExerciseResponse(
        UUID id, UUID exerciseId, String exerciseName, String exerciseImageUrl,
        int displayOrder, Integer sets, String reps, Integer durationSeconds,
        Integer restSeconds, BigDecimal weightKg, String notes
) {
    public static WorkoutExerciseResponse from(WorkoutExercise we) {
        return new WorkoutExerciseResponse(we.getId(), we.getExercise().getId(),
                we.getExercise().getName(), we.getExercise().getImageUrl(),
                we.getDisplayOrder(), we.getSets(), we.getReps(), we.getDurationSeconds(),
                we.getRestSeconds(), we.getWeightKg(), we.getNotes());
    }
}
