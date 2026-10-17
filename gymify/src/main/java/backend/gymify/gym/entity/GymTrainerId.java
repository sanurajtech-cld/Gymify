package backend.gymify.gym.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class GymTrainerId implements Serializable {

    private UUID gym;
    private UUID trainer;

    public GymTrainerId() {}

    public GymTrainerId(UUID gym, UUID trainer) {
        this.gym = gym;
        this.trainer = trainer;
    }

    public UUID getGym() { return gym; }
    public void setGym(UUID gym) { this.gym = gym; }

    public UUID getTrainer() { return trainer; }
    public void setTrainer(UUID trainer) { this.trainer = trainer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GymTrainerId that = (GymTrainerId) o;
        return Objects.equals(gym, that.gym) && Objects.equals(trainer, that.trainer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gym, trainer);
    }
}
