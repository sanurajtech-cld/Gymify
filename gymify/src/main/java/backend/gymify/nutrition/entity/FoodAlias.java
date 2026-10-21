package backend.gymify.nutrition.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "food_aliases", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"food_id", "alias"})
})
public class FoodAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodCatalog food;

    @Column(nullable = false, length = 200)
    private String alias;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public FoodCatalog getFood() { return food; }
    public void setFood(FoodCatalog food) { this.food = food; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
}
