package backend.gymify.workout.entity;

import backend.gymify.common.entity.BaseEntity;
import backend.gymify.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workout_plans")
public class WorkoutPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String goal;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "duration_weeks")
    private Integer durationWeeks;

    @Column(name = "is_template")
    private Boolean isTemplate = false;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayOfWeek ASC")
    private List<WorkoutDay> workoutDays = new ArrayList<>();

    public enum Difficulty {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getTrainer() { return trainer; }
    public void setTrainer(User trainer) { this.trainer = trainer; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Integer getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(Integer durationWeeks) { this.durationWeeks = durationWeeks; }

    public Boolean getIsTemplate() { return isTemplate; }
    public void setIsTemplate(Boolean isTemplate) { this.isTemplate = isTemplate; }

    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }

    public List<WorkoutDay> getWorkoutDays() { return workoutDays; }
    public void setWorkoutDays(List<WorkoutDay> workoutDays) { this.workoutDays = workoutDays; }
}
