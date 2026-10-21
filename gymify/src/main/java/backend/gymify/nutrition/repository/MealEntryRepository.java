package backend.gymify.nutrition.repository;

import backend.gymify.nutrition.entity.MealEntry;
import backend.gymify.nutrition.entity.MealEntry.MealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, UUID> {

    Page<MealEntry> findByUser_IdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Optional<MealEntry> findByIdAndUser_Id(UUID id, UUID userId);

    boolean existsByImageSha256AndUser_Id(String imageSha256, UUID userId);

    @Query("""
            SELECT m FROM MealEntry m
            WHERE m.user.id = :userId
            AND m.status = :status
            AND m.createdAt BETWEEN :from AND :to
            ORDER BY m.createdAt DESC
            """)
    List<MealEntry> findByUserAndStatusAndDateRange(
            @Param("userId") UUID userId,
            @Param("status") MealStatus status,
            @Param("from") Instant from,
            @Param("to") Instant to);
}
