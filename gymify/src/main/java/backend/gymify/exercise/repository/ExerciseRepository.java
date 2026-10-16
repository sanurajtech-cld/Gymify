package backend.gymify.exercise.repository;

import backend.gymify.exercise.entity.Exercise;
import backend.gymify.exercise.entity.Exercise.Difficulty;
import backend.gymify.exercise.entity.Exercise.ExerciseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    Page<Exercise> findByIsActiveTrue(Pageable pageable);

    Page<Exercise> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);

    Page<Exercise> findByDifficultyAndIsActiveTrue(Difficulty difficulty, Pageable pageable);

    Page<Exercise> findByExerciseTypeAndIsActiveTrue(ExerciseType exerciseType, Pageable pageable);

    @Query("SELECT e FROM Exercise e WHERE e.isActive = true AND " +
            "(LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Exercise> searchExercises(@Param("search") String search, Pageable pageable);

    @Query("SELECT DISTINCT e FROM Exercise e JOIN e.muscleGroups mg WHERE mg.id = :muscleGroupId AND e.isActive = true")
    Page<Exercise> findByMuscleGroupId(@Param("muscleGroupId") UUID muscleGroupId, Pageable pageable);

    @Query("SELECT DISTINCT e FROM Exercise e JOIN e.equipment eq WHERE eq.id = :equipmentId AND e.isActive = true")
    Page<Exercise> findByEquipmentId(@Param("equipmentId") UUID equipmentId, Pageable pageable);
}
