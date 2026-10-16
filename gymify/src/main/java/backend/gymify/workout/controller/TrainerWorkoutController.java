package backend.gymify.workout.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.workout.dto.*;
import backend.gymify.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trainer/workouts")
public class TrainerWorkoutController {

    private final WorkoutService workoutService;

    public TrainerWorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<WorkoutPlanResponse>>> getMyPlans(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getTrainerPlans(principal.getId())));
    }

    @PostMapping("/plans")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> createPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWorkoutPlanRequest request) {
        var response = workoutService.createPlan(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Plan created", response));
    }

    @PutMapping("/plans/{planId}/publish")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> publishPlan(
            @PathVariable UUID planId, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.publishPlan(planId, principal.getId())));
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<WorkoutAssignmentResponse>> assignPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AssignWorkoutRequest request) {
        var response = workoutService.assignPlan(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Plan assigned", response));
    }

    @GetMapping("/assignments")
    public ResponseEntity<ApiResponse<List<WorkoutAssignmentResponse>>> getMyAssignments(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getTrainerAssignments(principal.getId())));
    }
}
