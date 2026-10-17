package backend.gymify.workout.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record AssignWorkoutRequest(
        @NotNull UUID planId, @NotNull UUID customerId,
        UUID gymId, @NotNull LocalDate startDate, LocalDate endDate, String notes
) {}
