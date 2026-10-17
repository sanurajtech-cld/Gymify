package backend.gymify.workout.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.workout.dto.*;
import backend.gymify.workout.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/my-assignments")
    public ResponseEntity<ApiResponse<List<WorkoutAssignmentResponse>>> getMyAssignments(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getCustomerAssignments(principal.getId())));
    }

    @GetMapping("/plans/{planId}")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> getPlanById(@PathVariable UUID planId) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getPlanById(planId)));
    }
}
