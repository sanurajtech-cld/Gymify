package backend.gymify.membership.repository;

import backend.gymify.membership.entity.Payment;
import backend.gymify.membership.entity.Payment.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByUserId(UUID userId);

    Page<Payment> findByMembershipGymId(UUID gymId, Pageable pageable);

    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    List<Payment> findByMembershipIdAndStatus(UUID membershipId, PaymentStatus status);
}
