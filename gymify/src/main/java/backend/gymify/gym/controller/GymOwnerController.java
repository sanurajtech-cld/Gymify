package backend.gymify.gym.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.gym.dto.CreateGymRequest;
import backend.gymify.gym.dto.GymResponse;
import backend.gymify.gym.dto.UpdateGymRequest;
import backend.gymify.gym.service.GymService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/owner/gyms")
public class GymOwnerController {

    private final GymService gymService;

    public GymOwnerController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GymResponse>>> getMyGyms(@AuthenticationPrincipal UserPrincipal principal) {
        List<GymResponse> response = gymService.getOwnerGyms(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GymResponse>> createGym(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateGymRequest request) {
        GymResponse response = gymService.createGym(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Gym created successfully", response));
    }

    @PutMapping("/{gymId}")
    public ResponseEntity<ApiResponse<GymResponse>> updateGym(
            @PathVariable UUID gymId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateGymRequest request) {
        GymResponse response = gymService.updateGym(gymId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
