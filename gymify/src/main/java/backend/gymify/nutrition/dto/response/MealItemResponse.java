package backend.gymify.nutrition.dto.response;

import backend.gymify.nutrition.entity.MealItem;

import java.math.BigDecimal;
import java.util.UUID;

public record MealItemResponse(
        UUID id,
        String detectedFoodName,
        UUID foodCatalogId,
        BigDecimal estimatedWeightG,
        BigDecimal confirmedWeightG,
        BigDecimal confidenceScore,
        BigDecimal caloriesKcal,
        BigDecimal proteinG,
        BigDecimal carbsG,
        BigDecimal fatG,
        BigDecimal fibreG
) {
    public static MealItemResponse from(MealItem item) {
        return new MealItemResponse(
                item.getId(),
                item.getDetectedFoodName(),
                item.getFood() != null ? item.getFood().getId() : null,
                item.getEstimatedWeightG(),
                item.getConfirmedWeightG(),
                item.getConfidenceScore(),
                item.getCaloriesKcal(),
                item.getProteinG(),
                item.getCarbsG(),
                item.getFatG(),
                item.getFibreG()
        );
    }
}
