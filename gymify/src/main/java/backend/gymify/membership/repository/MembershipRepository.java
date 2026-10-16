package backend.gymify.membership.repository;

import backend.gymify.membership.entity.Membership;
import backend.gymify.membership.entity.Membership.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    List<Membership> findByUserId(UUID userId);

    List<Membership> findByUserIdAndStatus(UUID userId, MembershipStatus status);

    Page<Membership> findByGymId(UUID gymId, Pageable pageable);

    Page<Membership> findByGymIdAndStatus(UUID gymId, MembershipStatus status, Pageable pageable);

    Optional<Membership> findByUserIdAndGymIdAndStatus(UUID userId, UUID gymId, MembershipStatus status);

    @Query("SELECT m FROM Membership m WHERE m.status = 'ACTIVE' AND m.endDate <= :expiryDate")
    List<Membership> findExpiringMemberships(@Param("expiryDate") LocalDate expiryDate);

    @Query("SELECT m FROM Membership m WHERE m.assignedTrainer.id = :trainerId AND m.status = 'ACTIVE'")
    List<Membership> findActiveByTrainerId(@Param("trainerId") UUID trainerId);
}
