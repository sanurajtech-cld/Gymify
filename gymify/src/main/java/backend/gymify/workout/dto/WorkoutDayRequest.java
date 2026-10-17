package backend.gymify.workout.dto;

import java.util.List;

public record WorkoutDayRequest(
        int dayOfWeek, String name, boolean isRestDay, String notes,
        List<WorkoutExerciseRequest> exercises
) {}
