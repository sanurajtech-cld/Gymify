package backend.gymify.exercise.dto;

import backend.gymify.exercise.entity.ExerciseCategory;

import java.util.UUID;

public record ExerciseCategoryResponse(
        UUID id,
        String name,
        String description,
        String icon
) {
    public static ExerciseCategoryResponse from(ExerciseCategory category) {
        return new ExerciseCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIcon()
        );
    }
}
