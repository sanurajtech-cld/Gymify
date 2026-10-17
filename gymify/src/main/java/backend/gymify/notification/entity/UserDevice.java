package backend.gymify.notification.entity;

import backend.gymify.common.entity.BaseEntity;
import backend.gymify.user.entity.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_devices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "device_token"})
})
public class UserDevice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_token", nullable = false, length = 500)
    private String deviceToken;

    @Column(name = "device_type", length = 20)
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "device_name", length = 100)
    private String deviceName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public enum DeviceType {
        ANDROID, IOS
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getDeviceToken() { return deviceToken; }
    public void setDeviceToken(String deviceToken) { this.deviceToken = deviceToken; }

    public DeviceType getDeviceType() { return deviceType; }
    public void setDeviceType(DeviceType deviceType) { this.deviceType = deviceType; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
