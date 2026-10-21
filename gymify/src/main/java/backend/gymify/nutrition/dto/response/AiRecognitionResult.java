package backend.gymify.nutrition.dto.response;

import java.util.List;

public record AiRecognitionResult(
        List<DetectedFoodItem> foodItems,
        boolean needsUserConfirmation,
        String modelName,
        String promptVersion
) {}
