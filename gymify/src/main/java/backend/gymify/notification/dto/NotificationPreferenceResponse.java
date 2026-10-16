package backend.gymify.notification.dto;

import backend.gymify.notification.entity.NotificationPreference;
import java.time.LocalTime;

public record NotificationPreferenceResponse(
        boolean workoutReminders, boolean membershipAlerts, boolean planUpdates,
        boolean promotional, LocalTime reminderTime
) {
    public static NotificationPreferenceResponse from(NotificationPreference p) {
        return new NotificationPreferenceResponse(p.getWorkoutReminders(), p.getMembershipAlerts(),
                p.getPlanUpdates(), p.getPromotional(), p.getReminderTime());
    }
}
