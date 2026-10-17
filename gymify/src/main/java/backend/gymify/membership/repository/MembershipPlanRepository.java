package backend.gymify.membership.repository;

import backend.gymify.membership.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {

    List<MembershipPlan> findByGymIdAndIsActiveTrue(UUID gymId);

    List<MembershipPlan> findByGymId(UUID gymId);
}
