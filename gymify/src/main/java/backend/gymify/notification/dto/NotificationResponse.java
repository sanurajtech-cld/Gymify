package backend.gymify.notification.dto;

import backend.gymify.notification.entity.Notification;
import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id, String type, String title, String message, String data,
        boolean isRead, Instant readAt, Instant createdAt
) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(n.getId(), n.getType().name(), n.getTitle(),
                n.getMessage(), n.getData(), n.getIsRead(), n.getReadAt(), n.getCreatedAt());
    }
}
