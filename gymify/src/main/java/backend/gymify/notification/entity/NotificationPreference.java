package backend.gymify.notification.entity;

import backend.gymify.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "workout_reminders")
    private Boolean workoutReminders = true;

    @Column(name = "membership_alerts")
    private Boolean membershipAlerts = true;

    @Column(name = "plan_updates")
    private Boolean planUpdates = true;

    @Column
    private Boolean promotional = false;

    @Column(name = "reminder_time")
    private LocalTime reminderTime;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Boolean getWorkoutReminders() { return workoutReminders; }
    public void setWorkoutReminders(Boolean workoutReminders) { this.workoutReminders = workoutReminders; }

    public Boolean getMembershipAlerts() { return membershipAlerts; }
    public void setMembershipAlerts(Boolean membershipAlerts) { this.membershipAlerts = membershipAlerts; }

    public Boolean getPlanUpdates() { return planUpdates; }
    public void setPlanUpdates(Boolean planUpdates) { this.planUpdates = planUpdates; }

    public Boolean getPromotional() { return promotional; }
    public void setPromotional(Boolean promotional) { this.promotional = promotional; }

    public LocalTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalTime reminderTime) { this.reminderTime = reminderTime; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
