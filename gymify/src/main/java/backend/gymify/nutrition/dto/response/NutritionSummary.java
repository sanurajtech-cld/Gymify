package backend.gymify.nutrition.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record NutritionSummary(
        BigDecimal caloriesKcal,
        BigDecimal proteinG,
        BigDecimal carbsG,
        BigDecimal fatG,
        BigDecimal fibreG
) {
    public static NutritionSummary sumFrom(List<MealItemResponse> items) {
        BigDecimal cal = BigDecimal.ZERO, pro = BigDecimal.ZERO,
                carb = BigDecimal.ZERO, fat = BigDecimal.ZERO, fib = BigDecimal.ZERO;
        for (MealItemResponse i : items) {
            if (i.caloriesKcal() != null) cal = cal.add(i.caloriesKcal());
            if (i.proteinG() != null)     pro = pro.add(i.proteinG());
            if (i.carbsG() != null)       carb = carb.add(i.carbsG());
            if (i.fatG() != null)         fat = fat.add(i.fatG());
            if (i.fibreG() != null)       fib = fib.add(i.fibreG());
        }
        return new NutritionSummary(cal, pro, carb, fat, fib);
    }

    public static NutritionSummary fromRaw(Object[] row) {
        return new NutritionSummary(
                toBD(row[0]), toBD(row[1]), toBD(row[2]), toBD(row[3]), toBD(row[4])
        );
    }

    private static BigDecimal toBD(Object val) {
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal bd) return bd;
        return new BigDecimal(val.toString());
    }
}
