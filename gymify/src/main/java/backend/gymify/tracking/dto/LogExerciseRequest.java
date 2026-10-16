package backend.gymify.tracking.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record LogExerciseRequest(
        @NotNull UUID exerciseId, UUID workoutExerciseId, Integer setsCompleted,
        String repsCompleted, BigDecimal weightUsedKg, Integer durationSeconds,
        boolean isCompleted, String notes
) {}
