package backend.gymify.workout.repository;

import backend.gymify.workout.entity.WorkoutAssignment;
import backend.gymify.workout.entity.WorkoutAssignment.AssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutAssignmentRepository extends JpaRepository<WorkoutAssignment, UUID> {

    List<WorkoutAssignment> findByCustomerId(UUID customerId);

    List<WorkoutAssignment> findByCustomerIdAndStatus(UUID customerId, AssignmentStatus status);

    Optional<WorkoutAssignment> findByCustomerIdAndStatusAndPlanId(UUID customerId, AssignmentStatus status, UUID planId);

    Page<WorkoutAssignment> findByTrainerId(UUID trainerId, Pageable pageable);

    Page<WorkoutAssignment> findByTrainerIdAndStatus(UUID trainerId, AssignmentStatus status, Pageable pageable);

    List<WorkoutAssignment> findByGymIdAndStatus(UUID gymId, AssignmentStatus status);
}
