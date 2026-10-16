package backend.gymify.gym.repository;

import backend.gymify.gym.entity.GymTrainer;
import backend.gymify.gym.entity.GymTrainerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GymTrainerRepository extends JpaRepository<GymTrainer, GymTrainerId> {

    List<GymTrainer> findByGymIdAndIsActiveTrue(UUID gymId);

    List<GymTrainer> findByTrainerIdAndIsActiveTrue(UUID trainerId);
}
