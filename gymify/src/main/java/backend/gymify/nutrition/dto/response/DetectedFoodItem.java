package backend.gymify.nutrition.dto.response;

import java.math.BigDecimal;

public record DetectedFoodItem(
        String detectedName,
        BigDecimal estimatedWeightGrams,
        BigDecimal minimumWeightGrams,
        BigDecimal maximumWeightGrams,
        String confidence
) {}
