package backend.gymify.nutrition.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "meal_items")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private MealEntry mealEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private FoodCatalog food;

    @Column(name = "detected_food_name", nullable = false, length = 200)
    private String detectedFoodName;

    @Column(name = "estimated_weight_g", precision = 7, scale = 2)
    private BigDecimal estimatedWeightG;

    @Column(name = "confirmed_weight_g", precision = 7, scale = 2)
    private BigDecimal confirmedWeightG;

    @Column(name = "confidence_score", precision = 4, scale = 3)
    private BigDecimal confidenceScore;

    // Snapshot of calculated nutrients — immutable after confirmation
    @Column(name = "calories_kcal", precision = 7, scale = 2)
    private BigDecimal caloriesKcal;

    @Column(name = "protein_g", precision = 6, scale = 2)
    private BigDecimal proteinG;

    @Column(name = "carbs_g", precision = 6, scale = 2)
    private BigDecimal carbsG;

    @Column(name = "fat_g", precision = 6, scale = 2)
    private BigDecimal fatG;

    @Column(name = "fibre_g", precision = 6, scale = 2)
    private BigDecimal fibreG;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void onUpdate() { this.updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public MealEntry getMealEntry() { return mealEntry; }
    public void setMealEntry(MealEntry mealEntry) { this.mealEntry = mealEntry; }

    public FoodCatalog getFood() { return food; }
    public void setFood(FoodCatalog food) { this.food = food; }

    public String getDetectedFoodName() { return detectedFoodName; }
    public void setDetectedFoodName(String detectedFoodName) { this.detectedFoodName = detectedFoodName; }

    public BigDecimal getEstimatedWeightG() { return estimatedWeightG; }
    public void setEstimatedWeightG(BigDecimal estimatedWeightG) { this.estimatedWeightG = estimatedWeightG; }

    public BigDecimal getConfirmedWeightG() { return confirmedWeightG; }
    public void setConfirmedWeightG(BigDecimal confirmedWeightG) { this.confirmedWeightG = confirmedWeightG; }

    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }

    public BigDecimal getCaloriesKcal() { return caloriesKcal; }
    public void setCaloriesKcal(BigDecimal caloriesKcal) { this.caloriesKcal = caloriesKcal; }

    public BigDecimal getProteinG() { return proteinG; }
    public void setProteinG(BigDecimal proteinG) { this.proteinG = proteinG; }

    public BigDecimal getCarbsG() { return carbsG; }
    public void setCarbsG(BigDecimal carbsG) { this.carbsG = carbsG; }

    public BigDecimal getFatG() { return fatG; }
    public void setFatG(BigDecimal fatG) { this.fatG = fatG; }

    public BigDecimal getFibreG() { return fibreG; }
    public void setFibreG(BigDecimal fibreG) { this.fibreG = fibreG; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
