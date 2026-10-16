package backend.gymify.tracking.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.exercise.repository.ExerciseRepository;
import backend.gymify.tracking.dto.*;
import backend.gymify.tracking.entity.*;
import backend.gymify.tracking.entity.WorkoutLog.WorkoutLogStatus;
import backend.gymify.tracking.repository.*;
import backend.gymify.user.repository.UserRepository;
import backend.gymify.workout.repository.WorkoutAssignmentRepository;
import backend.gymify.workout.repository.WorkoutDayRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TrackingService {

    private final WorkoutLogRepository workoutLogRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final BodyMeasurementRepository measurementRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutAssignmentRepository assignmentRepository;
    private final WorkoutDayRepository workoutDayRepository;

    public TrackingService(WorkoutLogRepository workoutLogRepository, ExerciseLogRepository exerciseLogRepository,
                           BodyMeasurementRepository measurementRepository, UserRepository userRepository,
                           ExerciseRepository exerciseRepository, WorkoutAssignmentRepository assignmentRepository,
                           WorkoutDayRepository workoutDayRepository) {
        this.workoutLogRepository = workoutLogRepository;
        this.exerciseLogRepository = exerciseLogRepository;
        this.measurementRepository = measurementRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.assignmentRepository = assignmentRepository;
        this.workoutDayRepository = workoutDayRepository;
    }

    @Transactional
    public WorkoutLogResponse startWorkout(UUID userId, LogWorkoutRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        WorkoutLog log = new WorkoutLog();
        log.setUser(user);
        log.setWorkoutDate(request.workoutDate());
        log.setStartedAt(Instant.now());
        log.setStatus(WorkoutLogStatus.IN_PROGRESS);
        log.setNotes(request.notes());

        if (request.assignmentId() != null) {
            log.setAssignment(assignmentRepository.findById(request.assignmentId()).orElse(null));
        }
        if (request.workoutDayId() != null) {
            log.setWorkoutDay(workoutDayRepository.findById(request.workoutDayId()).orElse(null));
        }

        log = workoutLogRepository.save(log);

        if (request.exercises() != null) {
            for (var exReq : request.exercises()) {
                var exercise = exerciseRepository.findById(exReq.exerciseId())
                        .orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exReq.exerciseId()));
                ExerciseLog exLog = new ExerciseLog();
                exLog.setWorkoutLog(log);
                exLog.setExercise(exercise);
                exLog.setSetsCompleted(exReq.setsCompleted());
                exLog.setRepsCompleted(exReq.repsCompleted());
                exLog.setWeightUsedKg(exReq.weightUsedKg());
                exLog.setDurationSeconds(exReq.durationSeconds());
                exLog.setIsCompleted(exReq.isCompleted());
                exLog.setNotes(exReq.notes());
                log.getExerciseLogs().add(exLog);
            }
            log = workoutLogRepository.save(log);
        }

        return WorkoutLogResponse.from(log);
    }

    @Transactional
    public WorkoutLogResponse completeWorkout(UUID workoutLogId, UUID userId) {
        var log = workoutLogRepository.findById(workoutLogId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutLog", "id", workoutLogId));
        log.setCompletedAt(Instant.now());
        log.setStatus(WorkoutLogStatus.COMPLETED);
        if (log.getStartedAt() != null) {
            log.setDurationMinutes((int) ((Instant.now().toEpochMilli() - log.getStartedAt().toEpochMilli()) / 60000));
        }
        return WorkoutLogResponse.from(workoutLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public PageResponse<WorkoutLogResponse> getUserWorkoutLogs(UUID userId, Pageable pageable) {
        var page = workoutLogRepository.findByUserId(userId, pageable).map(WorkoutLogResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public List<WorkoutLogResponse> getWorkoutLogsByDateRange(UUID userId, LocalDate start, LocalDate end) {
        return workoutLogRepository.findByUserIdAndWorkoutDateBetween(userId, start, end)
                .stream().map(WorkoutLogResponse::from).toList();
    }

    @Transactional
    public BodyMeasurementResponse addMeasurement(UUID userId, CreateBodyMeasurementRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        BodyMeasurement m = new BodyMeasurement();
        m.setUser(user);
        m.setMeasuredAt(request.measuredAt());
        m.setWeightKg(request.weightKg());
        m.setHeightCm(request.heightCm());
        m.setBodyFatPercentage(request.bodyFatPercentage());
        m.setChestCm(request.chestCm());
        m.setWaistCm(request.waistCm());
        m.setHipsCm(request.hipsCm());
        m.setBicepsCm(request.bicepsCm());
        m.setThighsCm(request.thighsCm());
        m.setNotes(request.notes());

        return BodyMeasurementResponse.from(measurementRepository.save(m));
    }

    @Transactional(readOnly = true)
    public PageResponse<BodyMeasurementResponse> getUserMeasurements(UUID userId, Pageable pageable) {
        var page = measurementRepository.findByUserIdOrderByMeasuredAtDesc(userId, pageable)
                .map(BodyMeasurementResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public List<BodyMeasurementResponse> getMeasurementHistory(UUID userId, LocalDate start, LocalDate end) {
        return measurementRepository.findByUserIdAndMeasuredAtBetweenOrderByMeasuredAtAsc(userId, start, end)
                .stream().map(BodyMeasurementResponse::from).toList();
    }
}
