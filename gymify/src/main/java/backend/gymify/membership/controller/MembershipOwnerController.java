package backend.gymify.membership.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.membership.dto.*;
import backend.gymify.membership.service.MembershipService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/owner/memberships")
public class MembershipOwnerController {

    private final MembershipService membershipService;

    public MembershipOwnerController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/plans/{gymId}")
    public ResponseEntity<ApiResponse<MembershipPlanResponse>> createPlan(
            @PathVariable UUID gymId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateMembershipPlanRequest request) {
        MembershipPlanResponse response = membershipService.createPlan(gymId, principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Plan created", response));
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<ApiResponse<PageResponse<MembershipResponse>>> getGymMemberships(
            @PathVariable UUID gymId,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<MembershipResponse> response = membershipService.getGymMemberships(gymId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{membershipId}/assign-trainer/{trainerId}")
    public ResponseEntity<ApiResponse<MembershipResponse>> assignTrainer(
            @PathVariable UUID membershipId,
            @PathVariable UUID trainerId) {
        MembershipResponse response = membershipService.assignTrainer(membershipId, trainerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/payments/{gymId}")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getGymPayments(
            @PathVariable UUID gymId,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<PaymentResponse> response = membershipService.getGymPayments(gymId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
