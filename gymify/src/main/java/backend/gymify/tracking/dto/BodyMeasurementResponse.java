package backend.gymify.tracking.dto;

import backend.gymify.tracking.entity.BodyMeasurement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BodyMeasurementResponse(
        UUID id, LocalDate measuredAt, BigDecimal weightKg, BigDecimal heightCm,
        BigDecimal bodyFatPercentage, BigDecimal chestCm, BigDecimal waistCm,
        BigDecimal hipsCm, BigDecimal bicepsCm, BigDecimal thighsCm, String notes
) {
    public static BodyMeasurementResponse from(BodyMeasurement m) {
        return new BodyMeasurementResponse(m.getId(), m.getMeasuredAt(), m.getWeightKg(),
                m.getHeightCm(), m.getBodyFatPercentage(), m.getChestCm(), m.getWaistCm(),
                m.getHipsCm(), m.getBicepsCm(), m.getThighsCm(), m.getNotes());
    }
}
