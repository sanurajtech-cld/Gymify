package backend.gymify.gym.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.gym.dto.GymResponse;
import backend.gymify.gym.service.GymService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gyms")
public class GymController {

    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GymResponse>>> getGyms(
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<GymResponse> response = gymService.getApprovedGyms(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{gymId}")
    public ResponseEntity<ApiResponse<GymResponse>> getGymById(@PathVariable UUID gymId) {
        GymResponse response = gymService.getGymById(gymId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<GymResponse>>> searchGyms(
            @RequestParam String query,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<GymResponse> response = gymService.searchGyms(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<GymResponse>>> getNearbyGyms(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") double radiusKm) {
        List<GymResponse> response = gymService.findNearbyGyms(latitude, longitude, radiusKm);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<PageResponse<GymResponse>>> getGymsByCity(
            @PathVariable String city,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<GymResponse> response = gymService.getGymsByCity(city, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
