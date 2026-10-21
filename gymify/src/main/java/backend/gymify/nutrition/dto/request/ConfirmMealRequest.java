package backend.gymify.nutrition.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ConfirmMealRequest(
        @NotEmpty @Valid List<ConfirmMealItemRequest> items
) {}
