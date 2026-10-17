package backend.gymify.tracking.repository;

import backend.gymify.tracking.entity.WorkoutLog;
import backend.gymify.tracking.entity.WorkoutLog.WorkoutLogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {

    Page<WorkoutLog> findByUser_Id(UUID userId, Pageable pageable);

    List<WorkoutLog> findByUser_IdAndWorkoutDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);

    List<WorkoutLog> findByUser_IdAndStatus(UUID userId, WorkoutLogStatus status);

    @Query("SELECT wl FROM WorkoutLog wl WHERE wl.assignment.id = :assignmentId ORDER BY wl.workoutDate DESC")
    List<WorkoutLog> findByAssignmentId(@Param("assignmentId") UUID assignmentId);

    @Query("SELECT COUNT(wl) FROM WorkoutLog wl WHERE wl.user.id = :userId AND wl.status = 'COMPLETED' " +
            "AND wl.workoutDate BETWEEN :startDate AND :endDate")
    long countCompletedWorkouts(@Param("userId") UUID userId,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);
}
