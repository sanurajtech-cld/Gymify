package backend.gymify.exercise.dto;

import backend.gymify.exercise.entity.Equipment;

import java.util.UUID;

public record EquipmentResponse(
        UUID id,
        String name,
        String description,
        String imageUrl
) {
    public static EquipmentResponse from(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getImageUrl()
        );
    }
}
