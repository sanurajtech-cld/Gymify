package backend.gymify.nutrition.service;

import backend.gymify.nutrition.entity.FoodCatalog;
import backend.gymify.nutrition.entity.MealItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NutritionCalculationService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public void calculateAndApply(MealItem item, FoodCatalog food, BigDecimal weightGrams) {
        item.setCaloriesKcal(calculate(weightGrams, food.getCaloriesKcalPer100g()));
        item.setProteinG(calculate(weightGrams, food.getProteinGPer100g()));
        item.setCarbsG(calculate(weightGrams, food.getCarbsGPer100g()));
        item.setFatG(calculate(weightGrams, food.getFatGPer100g()));
        item.setFibreG(food.getFibreGPer100g() != null
                ? calculate(weightGrams, food.getFibreGPer100g())
                : BigDecimal.ZERO);
    }

    private BigDecimal calculate(BigDecimal weightGrams, BigDecimal nutrientPer100g) {
        if (weightGrams == null || nutrientPer100g == null
                || weightGrams.signum() < 0 || nutrientPer100g.signum() < 0) {
            throw new IllegalArgumentException("Weight and nutrient values must be non-negative");
        }
        return weightGrams
                .multiply(nutrientPer100g)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }
}
