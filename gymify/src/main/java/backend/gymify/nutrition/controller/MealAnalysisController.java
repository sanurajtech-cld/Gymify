package backend.gymify.nutrition.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.nutrition.dto.request.ConfirmMealRequest;
import backend.gymify.nutrition.dto.response.MealAnalysisResponse;
import backend.gymify.nutrition.service.MealService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/meal-analyses")
public class MealAnalysisController {

    private final MealService mealService;

    public MealAnalysisController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MealAnalysisResponse>> analyzeImage(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestPart("image") MultipartFile image) {
        var response = mealService.analyzeImage(principal.getId(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MealAnalysisResponse>> getAnalysis(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(mealService.getAnalysis(id, principal.getId())));
    }

    @PatchMapping("/{id}/items")
    public ResponseEntity<ApiResponse<MealAnalysisResponse>> updateItems(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ConfirmMealRequest request) {
        return ResponseEntity.ok(ApiResponse.success(mealService.confirmMeal(id, principal.getId(), request)));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<MealAnalysisResponse>> confirmMeal(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ConfirmMealRequest request) {
        return ResponseEntity.ok(ApiResponse.success(mealService.confirmMeal(id, principal.getId(), request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MealAnalysisResponse>>> getMealHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(mealService.getMealHistory(principal.getId(), pageable)));
    }
}
