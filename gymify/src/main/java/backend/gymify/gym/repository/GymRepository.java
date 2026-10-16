package backend.gymify.gym.repository;

import backend.gymify.gym.entity.Gym;
import backend.gymify.gym.entity.Gym.GymStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GymRepository extends JpaRepository<Gym, UUID> {

    List<Gym> findByOwnerId(UUID ownerId);

    Page<Gym> findByStatus(GymStatus status, Pageable pageable);

    Page<Gym> findByStatusAndIsActiveTrue(GymStatus status, Pageable pageable);

    Page<Gym> findByCityIgnoreCaseAndStatusAndIsActiveTrue(String city, GymStatus status, Pageable pageable);

    @Query("SELECT g FROM Gym g WHERE g.status = :status AND g.isActive = true " +
            "AND LOWER(g.city) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(g.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Gym> searchGyms(@Param("search") String search, @Param("status") GymStatus status, Pageable pageable);

    @Query(value = "SELECT * FROM gyms g WHERE g.status = 'APPROVED' AND g.is_active = true " +
            "AND ST_DWithin(g.location, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, :radiusMeters)",
            nativeQuery = true)
    List<Gym> findNearbyGyms(@Param("lat") double latitude, @Param("lng") double longitude,
                             @Param("radiusMeters") double radiusMeters);
}
