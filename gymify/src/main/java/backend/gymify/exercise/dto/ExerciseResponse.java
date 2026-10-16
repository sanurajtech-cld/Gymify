package backend.gymify.exercise.dto;

import backend.gymify.exercise.entity.Exercise;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ExerciseResponse(
        UUID id,
        String name,
        String description,
        String instructions,
        UUID categoryId,
        String categoryName,
        String difficulty,
        String exerciseType,
        String imageUrl,
        String videoUrl,
        String thumbnailUrl,
        BigDecimal caloriesPerMinute,
        List<String> muscleGroups,
        List<String> equipment
) {
    public static ExerciseResponse from(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getInstructions(),
                exercise.getCategory() != null ? exercise.getCategory().getId() : null,
                exercise.getCategory() != null ? exercise.getCategory().getName() : null,
                exercise.getDifficulty() != null ? exercise.getDifficulty().name() : null,
                exercise.getExerciseType() != null ? exercise.getExerciseType().name() : null,
                exercise.getImageUrl(),
                exercise.getVideoUrl(),
                exercise.getThumbnailUrl(),
                exercise.getCaloriesPerMinute(),
                exercise.getMuscleGroups().stream().map(m -> m.getName()).toList(),
                exercise.getEquipment().stream().map(e -> e.getName()).toList()
        );
    }
}
