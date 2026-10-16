package backend.gymify.notification.controller;

import backend.gymify.common.dto.ApiResponse;
import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.security.UserPrincipal;
import backend.gymify.notification.dto.*;
import backend.gymify.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal UserPrincipal principal, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUserNotifications(principal.getId(), pageable)));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal principal, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadNotifications(principal.getId(), pageable)));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadCount(principal.getId())));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable UUID notificationId, @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAsRead(notificationId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAllAsRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }

    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> getPreferences(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getPreferences(principal.getId())));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> updatePreferences(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateNotificationPreferenceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.updatePreferences(principal.getId(), request)));
    }

    @PostMapping("/devices")
    public ResponseEntity<ApiResponse<Void>> registerDevice(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody RegisterDeviceRequest request) {
        notificationService.registerDevice(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Device registered", null));
    }

    @DeleteMapping("/devices/{deviceToken}")
    public ResponseEntity<ApiResponse<Void>> unregisterDevice(@PathVariable String deviceToken) {
        notificationService.unregisterDevice(deviceToken);
        return ResponseEntity.ok(ApiResponse.success("Device unregistered", null));
    }
}
