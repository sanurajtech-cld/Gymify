package backend.gymify.workout.dto;

import backend.gymify.workout.entity.WorkoutDay;
import java.util.List;
import java.util.UUID;

public record WorkoutDayResponse(
        UUID id, int dayOfWeek, String name, boolean isRestDay, String notes,
        List<WorkoutExerciseResponse> exercises
) {
    public static WorkoutDayResponse from(WorkoutDay day) {
        return new WorkoutDayResponse(day.getId(), day.getDayOfWeek(), day.getName(),
                day.getIsRestDay(), day.getNotes(),
                day.getExercises().stream().map(WorkoutExerciseResponse::from).toList());
    }
}
