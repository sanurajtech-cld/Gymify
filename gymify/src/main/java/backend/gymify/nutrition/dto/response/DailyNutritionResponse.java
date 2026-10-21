package backend.gymify.nutrition.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DailyNutritionResponse(
        LocalDate date,
        NutritionSummary totals,
        List<MealAnalysisResponse> meals
) {}
