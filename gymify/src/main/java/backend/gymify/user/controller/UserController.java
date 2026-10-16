package backend.gymify.user.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.user.dto.ChangePasswordRequest;
import backend.gymify.user.dto.UpdateProfileRequest;
import backend.gymify.user.dto.UserResponse;
import backend.gymify.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        UserResponse response = userService.getProfile(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserResponse response = userService.updateProfile(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deactivateAccount(@AuthenticationPrincipal UserPrincipal principal) {
        userService.deactivateAccount(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Account deactivated", null));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
