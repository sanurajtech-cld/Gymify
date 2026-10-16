package backend.gymify.workout.repository;

import backend.gymify.workout.entity.WorkoutDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, UUID> {

    List<WorkoutDay> findByPlanIdOrderByDayOfWeekAsc(UUID planId);
}
