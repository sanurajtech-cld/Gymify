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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/plans/{gymId}")
    public ResponseEntity<ApiResponse<List<MembershipPlanResponse>>> getGymPlans(@PathVariable UUID gymId) {
        List<MembershipPlanResponse> response = membershipService.getGymPlans(gymId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<MembershipResponse>>> getMyMemberships(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<MembershipResponse> response = membershipService.getUserMemberships(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<ApiResponse<MembershipResponse>> getMembershipById(@PathVariable UUID membershipId) {
        MembershipResponse response = membershipService.getMembershipById(membershipId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/purchase/{gymId}")
    public ResponseEntity<ApiResponse<MembershipResponse>> purchaseMembership(
            @PathVariable UUID gymId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PurchaseMembershipRequest request) {
        MembershipResponse response = membershipService.purchaseMembership(principal.getId(), gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Membership purchased", response));
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<PaymentResponse> response = membershipService.getUserPayments(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
