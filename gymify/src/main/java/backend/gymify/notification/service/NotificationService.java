package backend.gymify.notification.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.notification.dto.*;
import backend.gymify.notification.entity.*;
import backend.gymify.notification.entity.UserDevice.DeviceType;
import backend.gymify.notification.repository.*;
import backend.gymify.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final UserDeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationPreferenceRepository preferenceRepository,
                               UserDeviceRepository deviceRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getUserNotifications(UUID userId, Pageable pageable) {
        var page = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(NotificationResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getUnreadNotifications(UUID userId, Pageable pageable) {
        var page = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId, pageable)
                .map(NotificationResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(UUID notificationId, UUID userId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setIsRead(true);
        notification.setReadAt(Instant.now());
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsRead(userId, Instant.now());
    }

    @Transactional(readOnly = true)
    public NotificationPreferenceResponse getPreferences(UUID userId) {
        return preferenceRepository.findByUserId(userId)
                .map(NotificationPreferenceResponse::from)
                .orElse(new NotificationPreferenceResponse(true, true, true, false, null));
    }

    @Transactional
    public NotificationPreferenceResponse updatePreferences(UUID userId, UpdateNotificationPreferenceRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        var pref = preferenceRepository.findByUserId(userId).orElseGet(() -> {
            var p = new NotificationPreference();
            p.setUser(user);
            return p;
        });

        if (request.workoutReminders() != null) pref.setWorkoutReminders(request.workoutReminders());
        if (request.membershipAlerts() != null) pref.setMembershipAlerts(request.membershipAlerts());
        if (request.planUpdates() != null) pref.setPlanUpdates(request.planUpdates());
        if (request.promotional() != null) pref.setPromotional(request.promotional());
        if (request.reminderTime() != null) pref.setReminderTime(request.reminderTime());
        pref.setUpdatedAt(Instant.now());

        return NotificationPreferenceResponse.from(preferenceRepository.save(pref));
    }

    @Transactional
    public void registerDevice(UUID userId, RegisterDeviceRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        var existing = deviceRepository.findByUserIdAndDeviceToken(userId, request.deviceToken());
        if (existing.isPresent()) return;

        UserDevice device = new UserDevice();
        device.setUser(user);
        device.setDeviceToken(request.deviceToken());
        if (request.deviceType() != null) device.setDeviceType(DeviceType.valueOf(request.deviceType()));
        device.setDeviceName(request.deviceName());
        deviceRepository.save(device);
    }

    @Transactional
    public void unregisterDevice(String deviceToken) {
        deviceRepository.deleteByDeviceToken(deviceToken);
    }
}
