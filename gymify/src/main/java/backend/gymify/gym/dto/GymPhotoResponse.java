package backend.gymify.gym.dto;

import backend.gymify.gym.entity.GymPhoto;

import java.util.UUID;

public record GymPhotoResponse(
        UUID id,
        String imageUrl,
        String caption,
        int displayOrder
) {
    public static GymPhotoResponse from(GymPhoto photo) {
        return new GymPhotoResponse(
                photo.getId(),
                photo.getImageUrl(),
                photo.getCaption(),
                photo.getDisplayOrder()
        );
    }
}
