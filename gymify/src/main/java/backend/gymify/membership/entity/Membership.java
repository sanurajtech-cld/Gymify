package backend.gymify.membership.entity;

import backend.gymify.common.entity.BaseEntity;
import backend.gymify.gym.entity.Gym;
import backend.gymify.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "memberships")
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_trainer_id")
    private User assignedTrainer;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    @Column(name = "freeze_start_date")
    private LocalDate freezeStartDate;

    @Column(name = "freeze_end_date")
    private LocalDate freezeEndDate;

    @Column(name = "total_freeze_days_used")
    private Integer totalFreezeDaysUsed = 0;

    public enum MembershipStatus {
        ACTIVE, EXPIRED, FROZEN, CANCELLED
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Gym getGym() { return gym; }
    public void setGym(Gym gym) { this.gym = gym; }

    public MembershipPlan getPlan() { return plan; }
    public void setPlan(MembershipPlan plan) { this.plan = plan; }

    public User getAssignedTrainer() { return assignedTrainer; }
    public void setAssignedTrainer(User assignedTrainer) { this.assignedTrainer = assignedTrainer; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public MembershipStatus getStatus() { return status; }
    public void setStatus(MembershipStatus status) { this.status = status; }

    public LocalDate getFreezeStartDate() { return freezeStartDate; }
    public void setFreezeStartDate(LocalDate freezeStartDate) { this.freezeStartDate = freezeStartDate; }

    public LocalDate getFreezeEndDate() { return freezeEndDate; }
    public void setFreezeEndDate(LocalDate freezeEndDate) { this.freezeEndDate = freezeEndDate; }

    public Integer getTotalFreezeDaysUsed() { return totalFreezeDaysUsed; }
    public void setTotalFreezeDaysUsed(Integer totalFreezeDaysUsed) { this.totalFreezeDaysUsed = totalFreezeDaysUsed; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }
}
