package backend.gymify.notification.dto;

import java.time.LocalTime;

public record UpdateNotificationPreferenceRequest(
        Boolean workoutReminders, Boolean membershipAlerts, Boolean planUpdates,
        Boolean promotional, LocalTime reminderTime
) {}
