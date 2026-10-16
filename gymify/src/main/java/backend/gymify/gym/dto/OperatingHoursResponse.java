package backend.gymify.gym.dto;

import backend.gymify.gym.entity.GymOperatingHours;

import java.time.LocalTime;

public record OperatingHoursResponse(
        int dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime,
        boolean isClosed
) {
    public static OperatingHoursResponse from(GymOperatingHours hours) {
        return new OperatingHoursResponse(
                hours.getDayOfWeek(),
                hours.getOpenTime(),
                hours.getCloseTime(),
                hours.getIsClosed()
        );
    }
}
