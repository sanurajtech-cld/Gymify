package backend.gymify.notification.repository;

import backend.gymify.notification.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

    List<UserDevice> findByUserIdAndIsActiveTrue(UUID userId);

    Optional<UserDevice> findByUserIdAndDeviceToken(UUID userId, String deviceToken);

    void deleteByDeviceToken(String deviceToken);
}
