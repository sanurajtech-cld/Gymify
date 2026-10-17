package backend.gymify.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDeviceRequest(
        @NotBlank String deviceToken, String deviceType, String deviceName
) {}
