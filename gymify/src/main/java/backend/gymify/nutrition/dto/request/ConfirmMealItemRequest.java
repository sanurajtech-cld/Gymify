package backend.gymify.nutrition.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ConfirmMealItemRequest(
        @NotNull UUID itemId,
        @NotBlank String foodName,
        UUID foodCatalogId,
        @NotNull @DecimalMin("0.1") BigDecimal confirmedWeightG
) {}
