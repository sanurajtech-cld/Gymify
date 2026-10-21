package backend.gymify.nutrition.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.nutrition.dto.response.DailyNutritionResponse;
import backend.gymify.nutrition.dto.response.FoodCatalogResponse;
import backend.gymify.nutrition.service.FoodCatalogService;
import backend.gymify.nutrition.service.MealService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/nutrition")
public class NutritionController {

    private final MealService mealService;
    private final FoodCatalogService foodCatalogService;

    public NutritionController(MealService mealService, FoodCatalogService foodCatalogService) {
        this.mealService = mealService;
        this.foodCatalogService = foodCatalogService;
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<DailyNutritionResponse>> getDailyNutrition(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(mealService.getDailyNutrition(principal.getId(), date)));
    }

    @GetMapping("/foods")
    public ResponseEntity<ApiResponse<PageResponse<FoodCatalogResponse>>> searchFoods(
            @RequestParam(required = false, defaultValue = "") String q,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(foodCatalogService.search(q, pageable)));
    }
}
