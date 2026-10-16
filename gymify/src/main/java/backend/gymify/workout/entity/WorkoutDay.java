package backend.gymify.workout.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workout_days", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"plan_id", "day_of_week"})
})
public class WorkoutDay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan plan;

    @Column(name = "day_of_week", nullable = false)
    private Short dayOfWeek;

    @Column(length = 100)
    private String name;

    @Column(name = "is_rest_day")
    private Boolean isRestDay = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "workoutDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public WorkoutPlan getPlan() { return plan; }
    public void setPlan(WorkoutPlan plan) { this.plan = plan; }

    public Short getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Short dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsRestDay() { return isRestDay; }
    public void setIsRestDay(Boolean isRestDay) { this.isRestDay = isRestDay; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<WorkoutExercise> getExercises() { return exercises; }
    public void setExercises(List<WorkoutExercise> exercises) { this.exercises = exercises; }
}
