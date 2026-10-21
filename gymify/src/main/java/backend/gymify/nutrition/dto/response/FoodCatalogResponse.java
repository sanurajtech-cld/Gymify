package backend.gymify.nutrition.dto.response;

import backend.gymify.nutrition.entity.FoodCatalog;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodCatalogResponse(
        UUID id,
        String canonicalName,
        String preparationState,
        String category,
        BigDecimal caloriesKcalPer100g,
        BigDecimal proteinGPer100g,
        BigDecimal carbsGPer100g,
        BigDecimal fatGPer100g,
        BigDecimal fibreGPer100g
) {
    public static FoodCatalogResponse from(FoodCatalog food) {
        return new FoodCatalogResponse(
                food.getId(),
                food.getCanonicalName(),
                food.getPreparationState(),
                food.getCategory(),
                food.getCaloriesKcalPer100g(),
                food.getProteinGPer100g(),
                food.getCarbsGPer100g(),
                food.getFatGPer100g(),
                food.getFibreGPer100g()
        );
    }
}
