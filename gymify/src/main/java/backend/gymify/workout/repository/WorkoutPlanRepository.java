package backend.gymify.workout.repository;

import backend.gymify.workout.entity.WorkoutPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {

    List<WorkoutPlan> findByTrainerId(UUID trainerId);

    Page<WorkoutPlan> findByTrainerIdAndIsPublishedTrue(UUID trainerId, Pageable pageable);

    List<WorkoutPlan> findByTrainerIdAndIsTemplateTrue(UUID trainerId);

    Page<WorkoutPlan> findByIsPublishedTrue(Pageable pageable);
}
