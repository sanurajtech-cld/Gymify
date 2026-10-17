package backend.gymify.tracking.dto;

import backend.gymify.tracking.entity.ExerciseLog;
import java.math.BigDecimal;
import java.util.UUID;

public record ExerciseLogResponse(
        UUID id, UUID exerciseId, String exerciseName, Integer setsCompleted,
        String repsCompleted, BigDecimal weightUsedKg, Integer durationSeconds,
        boolean isCompleted, String notes
) {
    public static ExerciseLogResponse from(ExerciseLog log) {
        return new ExerciseLogResponse(log.getId(), log.getExercise().getId(),
                log.getExercise().getName(), log.getSetsCompleted(), log.getRepsCompleted(),
                log.getWeightUsedKg(), log.getDurationSeconds(), log.getIsCompleted(), log.getNotes());
    }
}
