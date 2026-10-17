package backend.gymify.tracking.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record LogWorkoutRequest(
        UUID assignmentId, UUID workoutDayId, @NotNull LocalDate workoutDate,
        String notes, List<LogExerciseRequest> exercises
) {}
