package backend.gymify.exercise.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.exercise.dto.*;
import backend.gymify.exercise.service.ExerciseService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ExerciseResponse>>> getAllExercises(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getAllExercises(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseResponse>> getExerciseById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getExerciseById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ExerciseResponse>>> searchExercises(
            @RequestParam String query, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.searchExercises(query, pageable)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponse<ExerciseResponse>>> getByCategory(
            @PathVariable UUID categoryId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getByCategory(categoryId, pageable)));
    }

    @GetMapping("/muscle-group/{muscleGroupId}")
    public ResponseEntity<ApiResponse<PageResponse<ExerciseResponse>>> getByMuscleGroup(
            @PathVariable UUID muscleGroupId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getByMuscleGroup(muscleGroupId, pageable)));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<ExerciseCategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getAllCategories()));
    }

    @GetMapping("/muscle-groups")
    public ResponseEntity<ApiResponse<List<MuscleGroupResponse>>> getAllMuscleGroups() {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getAllMuscleGroups()));
    }

    @GetMapping("/equipment")
    public ResponseEntity<ApiResponse<List<EquipmentResponse>>> getAllEquipment() {
        return ResponseEntity.ok(ApiResponse.success(exerciseService.getAllEquipment()));
    }
}
