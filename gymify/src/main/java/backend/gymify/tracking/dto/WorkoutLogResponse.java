package backend.gymify.tracking.dto;

import backend.gymify.tracking.entity.WorkoutLog;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record WorkoutLogResponse(
        UUID id, UUID userId, UUID assignmentId, UUID workoutDayId, LocalDate workoutDate,
        Instant startedAt, Instant completedAt, Integer durationMinutes, Integer caloriesBurned,
        String status, String notes, List<ExerciseLogResponse> exerciseLogs
) {
    public static WorkoutLogResponse from(WorkoutLog log) {
        return new WorkoutLogResponse(log.getId(), log.getUser().getId(),
                log.getAssignment() != null ? log.getAssignment().getId() : null,
                log.getWorkoutDay() != null ? log.getWorkoutDay().getId() : null,
                log.getWorkoutDate(), log.getStartedAt(), log.getCompletedAt(),
                log.getDurationMinutes(), log.getCaloriesBurned(), log.getStatus().name(),
                log.getNotes(), log.getExerciseLogs().stream().map(ExerciseLogResponse::from).toList());
    }
}
