package backend.gymify.membership.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.exception.BadRequestException;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.gym.entity.Gym;
import backend.gymify.gym.repository.GymRepository;
import backend.gymify.membership.dto.*;
import backend.gymify.membership.entity.Membership;
import backend.gymify.membership.entity.Membership.MembershipStatus;
import backend.gymify.membership.entity.MembershipPlan;
import backend.gymify.membership.entity.Payment;
import backend.gymify.membership.entity.Payment.PaymentStatus;
import backend.gymify.membership.repository.MembershipPlanRepository;
import backend.gymify.membership.repository.MembershipRepository;
import backend.gymify.membership.repository.PaymentRepository;
import backend.gymify.user.entity.User;
import backend.gymify.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipPlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;

    public MembershipService(MembershipRepository membershipRepository, MembershipPlanRepository planRepository,
                             PaymentRepository paymentRepository, GymRepository gymRepository,
                             UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.planRepository = planRepository;
        this.paymentRepository = paymentRepository;
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
    }

    // Plan operations
    @Transactional(readOnly = true)
    public List<MembershipPlanResponse> getGymPlans(UUID gymId) {
        return planRepository.findByGymIdAndIsActiveTrue(gymId).stream()
                .map(MembershipPlanResponse::from)
                .toList();
    }

    @Transactional
    public MembershipPlanResponse createPlan(UUID gymId, UUID ownerId, CreateMembershipPlanRequest request) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResourceNotFoundException("Gym", "id", gymId));

        if (!gym.getOwner().getId().equals(ownerId)) {
            throw new BadRequestException("You don't own this gym");
        }

        MembershipPlan plan = new MembershipPlan();
        plan.setGym(gym);
        plan.setName(request.name());
        plan.setDescription(request.description());
        plan.setDurationDays(request.durationDays());
        plan.setPrice(request.price());
        plan.setCurrency(request.currency() != null ? request.currency() : "INR");
        plan.setIncludesTrainer(request.includesTrainer());
        plan.setMaxFreezeDays(request.maxFreezeDays() != null ? request.maxFreezeDays() : 0);

        plan = planRepository.save(plan);
        return MembershipPlanResponse.from(plan);
    }

    // Membership operations
    @Transactional(readOnly = true)
    public List<MembershipResponse> getUserMemberships(UUID userId) {
        return membershipRepository.findByUserId(userId).stream()
                .map(MembershipResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MembershipResponse getMembershipById(UUID membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", membershipId));
        return MembershipResponse.from(membership);
    }

    @Transactional
    public MembershipResponse purchaseMembership(UUID userId, UUID gymId, PurchaseMembershipRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        MembershipPlan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", request.planId()));

        if (!plan.getGym().getId().equals(gymId)) {
            throw new BadRequestException("Plan does not belong to this gym");
        }

        // Check for existing active membership
        membershipRepository.findByUserIdAndGymIdAndStatus(userId, gymId, MembershipStatus.ACTIVE)
                .ifPresent(m -> {
                    throw new BadRequestException("You already have an active membership at this gym");
                });

        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now();
        LocalDate endDate = startDate.plusDays(plan.getDurationDays());

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setGym(plan.getGym());
        membership.setPlan(plan);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setStatus(MembershipStatus.ACTIVE);

        if (request.trainerId() != null && plan.getIncludesTrainer()) {
            User trainer = userRepository.findById(request.trainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer", "id", request.trainerId()));
            membership.setAssignedTrainer(trainer);
        }

        membership = membershipRepository.save(membership);

        // Create payment record
        Payment payment = new Payment();
        payment.setMembership(membership);
        payment.setUser(user);
        payment.setAmount(plan.getPrice());
        payment.setCurrency(plan.getCurrency());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(Instant.now());
        paymentRepository.save(payment);

        return MembershipResponse.from(membership);
    }

    @Transactional(readOnly = true)
    public PageResponse<MembershipResponse> getGymMemberships(UUID gymId, Pageable pageable) {
        Page<MembershipResponse> page = membershipRepository.findByGymId(gymId, pageable)
                .map(MembershipResponse::from);
        return PageResponse.from(page);
    }

    @Transactional
    public MembershipResponse assignTrainer(UUID membershipId, UUID trainerId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", membershipId));

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer", "id", trainerId));

        membership.setAssignedTrainer(trainer);
        membership = membershipRepository.save(membership);
        return MembershipResponse.from(membership);
    }

    @Transactional(readOnly = true)
    public List<MembershipResponse> getTrainerAssignedMembers(UUID trainerId) {
        return membershipRepository.findActiveByTrainerId(trainerId).stream()
                .map(MembershipResponse::from)
                .toList();
    }

    // Payment operations
    @Transactional(readOnly = true)
    public List<PaymentResponse> getUserPayments(UUID userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> getGymPayments(UUID gymId, Pageable pageable) {
        Page<PaymentResponse> page = paymentRepository.findByMembershipGymId(gymId, pageable)
                .map(PaymentResponse::from);
        return PageResponse.from(page);
    }
}
