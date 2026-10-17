package backend.gymify.exercise.repository;

import backend.gymify.exercise.entity.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, UUID> {

    Optional<MuscleGroup> findByName(String name);

    List<MuscleGroup> findByBodyRegion(String bodyRegion);
}
