package backend.gymify.exercise.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateExerciseRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        String instructions,

        UUID categoryId,

        String difficulty,

        String exerciseType,

        String imageUrl,

        String videoUrl,

        String thumbnailUrl,

        BigDecimal caloriesPerMinute,

        List<UUID> muscleGroupIds,

        List<UUID> equipmentIds
) {}
