package backend.gymify.gym.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.exception.ForbiddenException;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.gym.dto.*;
import backend.gymify.gym.entity.*;
import backend.gymify.gym.entity.Gym.GymStatus;
import backend.gymify.gym.repository.FacilityRepository;
import backend.gymify.gym.repository.GymRepository;
import backend.gymify.user.entity.User;
import backend.gymify.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class GymService {

    private final GymRepository gymRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    public GymService(GymRepository gymRepository, FacilityRepository facilityRepository,
                      UserRepository userRepository) {
        this.gymRepository = gymRepository;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public GymResponse getGymById(UUID gymId) {
        Gym gym = findGymById(gymId);
        return GymResponse.from(gym);
    }

    @Transactional(readOnly = true)
    public PageResponse<GymResponse> getApprovedGyms(Pageable pageable) {
        Page<GymResponse> page = gymRepository.findByStatusAndIsActiveTrue(GymStatus.APPROVED, pageable)
                .map(GymResponse::summary);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<GymResponse> searchGyms(String query, Pageable pageable) {
        Page<GymResponse> page = gymRepository.searchGyms(query, GymStatus.APPROVED, pageable)
                .map(GymResponse::summary);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public List<GymResponse> findNearbyGyms(double latitude, double longitude, double radiusKm) {
        double radiusMeters = radiusKm * 1000;
        return gymRepository.findNearbyGyms(latitude, longitude, radiusMeters).stream()
                .map(GymResponse::summary)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<GymResponse> getGymsByCity(String city, Pageable pageable) {
        Page<GymResponse> page = gymRepository.findByCityIgnoreCaseAndStatusAndIsActiveTrue(city, GymStatus.APPROVED, pageable)
                .map(GymResponse::summary);
        return PageResponse.from(page);
    }

    @Transactional
    public GymResponse createGym(UUID ownerId, CreateGymRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", ownerId));

        Gym gym = new Gym();
        gym.setOwner(owner);
        gym.setName(request.name());
        gym.setDescription(request.description());
        gym.setLogoUrl(request.logoUrl());
        gym.setAddressLine1(request.addressLine1());
        gym.setAddressLine2(request.addressLine2());
        gym.setCity(request.city());
        gym.setState(request.state());
        gym.setCountry(request.country());
        gym.setPostalCode(request.postalCode());
        gym.setLatitude(request.latitude());
        gym.setLongitude(request.longitude());
        gym.setPhone(request.phone());
        gym.setEmail(request.email());
        gym.setWebsite(request.website());
        gym.setStatus(GymStatus.PENDING);

        if (request.facilityIds() != null && !request.facilityIds().isEmpty()) {
            var facilities = facilityRepository.findAllById(request.facilityIds());
            gym.setFacilities(new HashSet<>(facilities));
        }

        gym = gymRepository.save(gym);

        if (request.operatingHours() != null) {
            for (OperatingHoursRequest hoursReq : request.operatingHours()) {
                GymOperatingHours hours = new GymOperatingHours();
                hours.setGym(gym);
                hours.setDayOfWeek((short) hoursReq.dayOfWeek());
                hours.setOpenTime(hoursReq.openTime());
                hours.setCloseTime(hoursReq.closeTime());
                hours.setIsClosed(hoursReq.isClosed());
                gym.getOperatingHours().add(hours);
            }
            gym = gymRepository.save(gym);
        }

        return GymResponse.from(gym);
    }

    @Transactional
    public GymResponse updateGym(UUID gymId, UUID ownerId, UpdateGymRequest request) {
        Gym gym = findGymById(gymId);

        if (!gym.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("You don't have permission to update this gym");
        }

        if (request.name() != null) gym.setName(request.name());
        if (request.description() != null) gym.setDescription(request.description());
        if (request.logoUrl() != null) gym.setLogoUrl(request.logoUrl());
        if (request.addressLine1() != null) gym.setAddressLine1(request.addressLine1());
        if (request.addressLine2() != null) gym.setAddressLine2(request.addressLine2());
        if (request.city() != null) gym.setCity(request.city());
        if (request.state() != null) gym.setState(request.state());
        if (request.country() != null) gym.setCountry(request.country());
        if (request.postalCode() != null) gym.setPostalCode(request.postalCode());
        if (request.latitude() != null) gym.setLatitude(request.latitude());
        if (request.longitude() != null) gym.setLongitude(request.longitude());
        if (request.phone() != null) gym.setPhone(request.phone());
        if (request.email() != null) gym.setEmail(request.email());
        if (request.website() != null) gym.setWebsite(request.website());

        if (request.facilityIds() != null) {
            var facilities = facilityRepository.findAllById(request.facilityIds());
            gym.setFacilities(new HashSet<>(facilities));
        }

        if (request.operatingHours() != null) {
            gym.getOperatingHours().clear();
            for (OperatingHoursRequest hoursReq : request.operatingHours()) {
                GymOperatingHours hours = new GymOperatingHours();
                hours.setGym(gym);
                hours.setDayOfWeek((short) hoursReq.dayOfWeek());
                hours.setOpenTime(hoursReq.openTime());
                hours.setCloseTime(hoursReq.closeTime());
                hours.setIsClosed(hoursReq.isClosed());
                gym.getOperatingHours().add(hours);
            }
        }

        gym = gymRepository.save(gym);
        return GymResponse.from(gym);
    }

    @Transactional(readOnly = true)
    public List<GymResponse> getOwnerGyms(UUID ownerId) {
        return gymRepository.findByOwnerId(ownerId).stream()
                .map(GymResponse::from)
                .toList();
    }

    @Transactional
    public GymResponse updateGymStatus(UUID gymId, GymStatus status) {
        Gym gym = findGymById(gymId);
        gym.setStatus(status);
        gym = gymRepository.save(gym);
        return GymResponse.from(gym);
    }

    @Transactional(readOnly = true)
    public PageResponse<GymResponse> getPendingGyms(Pageable pageable) {
        Page<GymResponse> page = gymRepository.findByStatus(GymStatus.PENDING, pageable)
                .map(GymResponse::from);
        return PageResponse.from(page);
    }

    private Gym findGymById(UUID gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new ResourceNotFoundException("Gym", "id", gymId));
    }
}
