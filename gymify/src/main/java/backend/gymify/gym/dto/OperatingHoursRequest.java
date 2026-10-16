package backend.gymify.gym.dto;

import java.time.LocalTime;

public record OperatingHoursRequest(
        int dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime,
        boolean isClosed
) {}
