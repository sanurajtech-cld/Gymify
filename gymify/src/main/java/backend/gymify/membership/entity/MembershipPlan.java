package backend.gymify.membership.entity;

import backend.gymify.common.entity.BaseEntity;
import backend.gymify.gym.entity.Gym;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    private String currency = "INR";

    @Column(name = "includes_trainer")
    private Boolean includesTrainer = false;

    @Column(name = "max_freeze_days")
    private Integer maxFreezeDays = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Gym getGym() { return gym; }
    public void setGym(Gym gym) { this.gym = gym; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Boolean getIncludesTrainer() { return includesTrainer; }
    public void setIncludesTrainer(Boolean includesTrainer) { this.includesTrainer = includesTrainer; }

    public Integer getMaxFreezeDays() { return maxFreezeDays; }
    public void setMaxFreezeDays(Integer maxFreezeDays) { this.maxFreezeDays = maxFreezeDays; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
