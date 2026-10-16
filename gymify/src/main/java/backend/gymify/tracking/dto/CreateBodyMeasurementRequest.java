package backend.gymify.tracking.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBodyMeasurementRequest(
        @NotNull LocalDate measuredAt, BigDecimal weightKg, BigDecimal heightCm,
        BigDecimal bodyFatPercentage, BigDecimal chestCm, BigDecimal waistCm,
        BigDecimal hipsCm, BigDecimal bicepsCm, BigDecimal thighsCm, String notes
) {}
