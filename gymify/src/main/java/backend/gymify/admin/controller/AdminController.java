package backend.gymify.admin.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.exercise.dto.CreateExerciseRequest;
import backend.gymify.exercise.dto.ExerciseResponse;
import backend.gymify.exercise.service.ExerciseService;
import backend.gymify.gym.dto.GymResponse;
import backend.gymify.gym.entity.Gym.GymStatus;
import backend.gymify.gym.service.GymService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final GymService gymService;
    private final ExerciseService exerciseService;

    public AdminController(GymService gymService, ExerciseService exerciseService) {
        this.gymService = gymService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/gyms/pending")
    public ResponseEntity<ApiResponse<PageResponse<GymResponse>>> getPendingGyms(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(gymService.getPendingGyms(pageable)));
    }

    @PutMapping("/gyms/{gymId}/approve")
    public ResponseEntity<ApiResponse<GymResponse>> approveGym(@PathVariable UUID gymId) {
        return ResponseEntity.ok(ApiResponse.success(gymService.updateGymStatus(gymId, GymStatus.APPROVED)));
    }

    @PutMapping("/gyms/{gymId}/reject")
    public ResponseEntity<ApiResponse<GymResponse>> rejectGym(@PathVariable UUID gymId) {
        return ResponseEntity.ok(ApiResponse.success(gymService.updateGymStatus(gymId, GymStatus.REJECTED)));
    }

    @PutMapping("/gyms/{gymId}/suspend")
    public ResponseEntity<ApiResponse<GymResponse>> suspendGym(@PathVariable UUID gymId) {
        return ResponseEntity.ok(ApiResponse.success(gymService.updateGymStatus(gymId, GymStatus.SUSPENDED)));
    }

    @PostMapping("/exercises")
    public ResponseEntity<ApiResponse<ExerciseResponse>> createExercise(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateExerciseRequest request) {
        var response = exerciseService.createExercise(request, principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Exercise created", response));
    }
}
