package backend.gymify.workout.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateWorkoutPlanRequest(
        @NotBlank(message = "Name is required") String name,
        String description, String goal, String difficulty,
        Integer durationWeeks, boolean isTemplate,
        List<WorkoutDayRequest> days
) {}
