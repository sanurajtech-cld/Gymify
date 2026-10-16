package backend.gymify.exercise.repository;

import backend.gymify.exercise.entity.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategory, UUID> {

    Optional<ExerciseCategory> findByName(String name);
}
