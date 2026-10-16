package backend.gymify.gym.dto;

import backend.gymify.gym.entity.Gym;

import java.util.List;
import java.util.UUID;

public record GymResponse(
        UUID id,
        UUID ownerId,
        String name,
        String description,
        String logoUrl,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String postalCode,
        Double latitude,
        Double longitude,
        String phone,
        String email,
        String website,
        String status,
        boolean isActive,
        List<String> facilities,
        List<GymPhotoResponse> photos,
        List<OperatingHoursResponse> operatingHours
) {
    public static GymResponse from(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getOwner().getId(),
                gym.getName(),
                gym.getDescription(),
                gym.getLogoUrl(),
                gym.getAddressLine1(),
                gym.getAddressLine2(),
                gym.getCity(),
                gym.getState(),
                gym.getCountry(),
                gym.getPostalCode(),
                gym.getLatitude(),
                gym.getLongitude(),
                gym.getPhone(),
                gym.getEmail(),
                gym.getWebsite(),
                gym.getStatus().name(),
                gym.getIsActive(),
                gym.getFacilities().stream().map(f -> f.getName()).toList(),
                gym.getPhotos().stream().map(GymPhotoResponse::from).toList(),
                gym.getOperatingHours().stream().map(OperatingHoursResponse::from).toList()
        );
    }

    public static GymResponse summary(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getOwner().getId(),
                gym.getName(),
                gym.getDescription(),
                gym.getLogoUrl(),
                gym.getAddressLine1(),
                null,
                gym.getCity(),
                gym.getState(),
                gym.getCountry(),
                null,
                gym.getLatitude(),
                gym.getLongitude(),
                gym.getPhone(),
                null,
                null,
                gym.getStatus().name(),
                gym.getIsActive(),
                null,
                null,
                null
        );
    }
}
