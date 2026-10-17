package backend.gymify.membership.dto;

import backend.gymify.membership.entity.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID membershipId,
        UUID userId,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String gateway,
        String gatewayPaymentId,
        String status,
        Instant paidAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getMembership().getId(),
                payment.getUser().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentMethod(),
                payment.getGateway(),
                payment.getGatewayPaymentId(),
                payment.getStatus().name(),
                payment.getPaidAt()
        );
    }
}
