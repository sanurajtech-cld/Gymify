package backend.gymify.workout.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WorkoutExerciseRequest(
        UUID exerciseId, int displayOrder, Integer sets, String reps,
        Integer durationSeconds, Integer restSeconds, BigDecimal weightKg, String notes
) {}
