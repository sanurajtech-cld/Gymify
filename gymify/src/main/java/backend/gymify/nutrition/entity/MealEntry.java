package backend.gymify.nutrition.entity;

import backend.gymify.user.entity.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meal_entries")
public class MealEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MealStatus status = MealStatus.PROCESSING;

    @Column(name = "model_name", length = 100)
    private String modelName;

    @Column(name = "prompt_version", length = 50)
    private String promptVersion;

    @Column(name = "image_sha256", length = 64)
    private String imageSha256;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @OneToMany(mappedBy = "mealEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealItem> items = new ArrayList<>();

    public enum MealStatus {
        PROCESSING, NEEDS_CONFIRMATION, CONFIRMED, FAILED
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public MealStatus getStatus() { return status; }
    public void setStatus(MealStatus status) { this.status = status; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getPromptVersion() { return promptVersion; }
    public void setPromptVersion(String promptVersion) { this.promptVersion = promptVersion; }

    public String getImageSha256() { return imageSha256; }
    public void setImageSha256(String imageSha256) { this.imageSha256 = imageSha256; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(Instant confirmedAt) { this.confirmedAt = confirmedAt; }

    public List<MealItem> getItems() { return items; }
    public void setItems(List<MealItem> items) { this.items = items; }
}
