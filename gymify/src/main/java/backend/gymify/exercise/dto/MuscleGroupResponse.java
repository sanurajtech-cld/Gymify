package backend.gymify.exercise.dto;

import backend.gymify.exercise.entity.MuscleGroup;

import java.util.UUID;

public record MuscleGroupResponse(
        UUID id,
        String name,
        String bodyRegion,
        String imageUrl
) {
    public static MuscleGroupResponse from(MuscleGroup muscleGroup) {
        return new MuscleGroupResponse(
                muscleGroup.getId(),
                muscleGroup.getName(),
                muscleGroup.getBodyRegion(),
                muscleGroup.getImageUrl()
        );
    }
}
