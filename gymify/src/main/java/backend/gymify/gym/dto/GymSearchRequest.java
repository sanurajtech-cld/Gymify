package backend.gymify.gym.dto;

public record GymSearchRequest(
        String query,
        String city,
        Double latitude,
        Double longitude,
        Double radiusKm
) {}
