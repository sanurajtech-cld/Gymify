package backend.gymify.nutrition.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food_catalog")
public class FoodCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "canonical_name", nullable = false, length = 200)
    private String canonicalName;

    @Column(name = "preparation_state", length = 100)
    private String preparationState;

    @Column(length = 100)
    private String category;

    @Column(name = "source_name", length = 100)
    private String sourceName;

    @Column(name = "source_food_id", length = 100)
    private String sourceFoodId;

    @Column(name = "basis_grams", precision = 7, scale = 2)
    private BigDecimal basisGrams = new BigDecimal("100.00");

    @Column(name = "calories_kcal_per_100g", nullable = false, precision = 7, scale = 2)
    private BigDecimal caloriesKcalPer100g;

    @Column(name = "protein_g_per_100g", nullable = false, precision = 6, scale = 2)
    private BigDecimal proteinGPer100g;

    @Column(name = "carbs_g_per_100g", nullable = false, precision = 6, scale = 2)
    private BigDecimal carbsGPer100g;

    @Column(name = "fat_g_per_100g", nullable = false, precision = 6, scale = 2)
    private BigDecimal fatGPer100g;

    @Column(name = "fibre_g_per_100g", precision = 6, scale = 2)
    private BigDecimal fibreGPer100g;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodAlias> aliases = new ArrayList<>();

    @PreUpdate
    void onUpdate() { this.updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCanonicalName() { return canonicalName; }
    public void setCanonicalName(String canonicalName) { this.canonicalName = canonicalName; }

    public String getPreparationState() { return preparationState; }
    public void setPreparationState(String preparationState) { this.preparationState = preparationState; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public String getSourceFoodId() { return sourceFoodId; }
    public void setSourceFoodId(String sourceFoodId) { this.sourceFoodId = sourceFoodId; }

    public BigDecimal getBasisGrams() { return basisGrams; }
    public void setBasisGrams(BigDecimal basisGrams) { this.basisGrams = basisGrams; }

    public BigDecimal getCaloriesKcalPer100g() { return caloriesKcalPer100g; }
    public void setCaloriesKcalPer100g(BigDecimal caloriesKcalPer100g) { this.caloriesKcalPer100g = caloriesKcalPer100g; }

    public BigDecimal getProteinGPer100g() { return proteinGPer100g; }
    public void setProteinGPer100g(BigDecimal proteinGPer100g) { this.proteinGPer100g = proteinGPer100g; }

    public BigDecimal getCarbsGPer100g() { return carbsGPer100g; }
    public void setCarbsGPer100g(BigDecimal carbsGPer100g) { this.carbsGPer100g = carbsGPer100g; }

    public BigDecimal getFatGPer100g() { return fatGPer100g; }
    public void setFatGPer100g(BigDecimal fatGPer100g) { this.fatGPer100g = fatGPer100g; }

    public BigDecimal getFibreGPer100g() { return fibreGPer100g; }
    public void setFibreGPer100g(BigDecimal fibreGPer100g) { this.fibreGPer100g = fibreGPer100g; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<FoodAlias> getAliases() { return aliases; }
    public void setAliases(List<FoodAlias> aliases) { this.aliases = aliases; }
}
