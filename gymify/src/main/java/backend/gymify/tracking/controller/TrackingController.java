package backend.gymify.tracking.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.tracking.dto.*;
import backend.gymify.tracking.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PostMapping("/workouts")
    public ResponseEntity<ApiResponse<WorkoutLogResponse>> startWorkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody LogWorkoutRequest request) {
        var response = trackingService.startWorkout(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PutMapping("/workouts/{workoutLogId}/complete")
    public ResponseEntity<ApiResponse<WorkoutLogResponse>> completeWorkout(
            @PathVariable UUID workoutLogId, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(trackingService.completeWorkout(workoutLogId, principal.getId())));
    }

    @GetMapping("/workouts")
    public ResponseEntity<ApiResponse<PageResponse<WorkoutLogResponse>>> getMyWorkoutLogs(
            @AuthenticationPrincipal UserPrincipal principal, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(trackingService.getUserWorkoutLogs(principal.getId(), pageable)));
    }

    @GetMapping("/workouts/range")
    public ResponseEntity<ApiResponse<List<WorkoutLogResponse>>> getWorkoutsByDateRange(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.success(trackingService.getWorkoutLogsByDateRange(principal.getId(), start, end)));
    }

    @PostMapping("/measurements")
    public ResponseEntity<ApiResponse<BodyMeasurementResponse>> addMeasurement(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateBodyMeasurementRequest request) {
        var response = trackingService.addMeasurement(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/measurements")
    public ResponseEntity<ApiResponse<PageResponse<BodyMeasurementResponse>>> getMyMeasurements(
            @AuthenticationPrincipal UserPrincipal principal, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(trackingService.getUserMeasurements(principal.getId(), pageable)));
    }

    @GetMapping("/measurements/history")
    public ResponseEntity<ApiResponse<List<BodyMeasurementResponse>>> getMeasurementHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.success(trackingService.getMeasurementHistory(principal.getId(), start, end)));
    }
}
