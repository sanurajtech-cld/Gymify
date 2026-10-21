package backend.gymify.nutrition.repository;

import backend.gymify.nutrition.entity.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, UUID> {

    List<MealItem> findByMealEntry_Id(UUID mealEntryId);

    @Query("""
            SELECT COALESCE(SUM(mi.caloriesKcal), 0),
                   COALESCE(SUM(mi.proteinG), 0),
                   COALESCE(SUM(mi.carbsG), 0),
                   COALESCE(SUM(mi.fatG), 0),
                   COALESCE(SUM(mi.fibreG), 0)
            FROM MealItem mi
            JOIN mi.mealEntry me
            WHERE me.user.id = :userId
            AND me.status = 'CONFIRMED'
            AND me.confirmedAt BETWEEN :from AND :to
            """)
    Object[] sumNutrientsForUserAndDateRange(
            @Param("userId") UUID userId,
            @Param("from") Instant from,
            @Param("to") Instant to);
}
