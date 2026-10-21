package backend.gymify.nutrition.dto.response;

import backend.gymify.nutrition.entity.MealEntry;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MealAnalysisResponse(
        UUID id,
        MealEntry.MealStatus status,
        String modelName,
        List<MealItemResponse> items,
        NutritionSummary totals,
        Instant createdAt,
        Instant confirmedAt
) {
    public static MealAnalysisResponse from(MealEntry entry) {
        var items = entry.getItems().stream().map(MealItemResponse::from).toList();
        return new MealAnalysisResponse(
                entry.getId(),
                entry.getStatus(),
                entry.getModelName(),
                items,
                NutritionSummary.sumFrom(items),
                entry.getCreatedAt(),
                entry.getConfirmedAt()
        );
    }
}
