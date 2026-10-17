package backend.gymify.gym.entity;

import backend.gymify.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "gym_trainers")
@IdClass(GymTrainerId.class)
public class GymTrainer {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "joined_at")
    private Instant joinedAt = Instant.now();

    public Gym getGym() { return gym; }
    public void setGym(Gym gym) { this.gym = gym; }

    public User getTrainer() { return trainer; }
    public void setTrainer(User trainer) { this.trainer = trainer; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
}
