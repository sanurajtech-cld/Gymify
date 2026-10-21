package backend.gymify.nutrition.repository;

import backend.gymify.nutrition.entity.FoodCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FoodCatalogRepository extends JpaRepository<FoodCatalog, UUID> {

    Page<FoodCatalog> findByIsActiveTrueAndCanonicalNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<FoodCatalog> findByCanonicalNameIgnoreCaseAndIsActiveTrue(String canonicalName);

    @Query("""
            SELECT DISTINCT f FROM FoodCatalog f
            LEFT JOIN f.aliases a
            WHERE f.isActive = true
            AND (LOWER(f.canonicalName) LIKE LOWER(CONCAT('%', :term, '%'))
                 OR LOWER(a.alias) LIKE LOWER(CONCAT('%', :term, '%')))
            """)
    List<FoodCatalog> searchByNameOrAlias(@Param("term") String term);
}
