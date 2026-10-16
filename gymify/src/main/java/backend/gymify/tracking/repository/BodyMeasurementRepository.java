package backend.gymify.tracking.repository;

import backend.gymify.tracking.entity.BodyMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, UUID> {

    Page<BodyMeasurement> findByUserIdOrderByMeasuredAtDesc(UUID userId, Pageable pageable);

    List<BodyMeasurement> findByUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(
            UUID userId, LocalDate startDate, LocalDate endDate);

    Optional<BodyMeasurement> findFirstByUserIdOrderByMeasuredAtDesc(UUID userId);
}
