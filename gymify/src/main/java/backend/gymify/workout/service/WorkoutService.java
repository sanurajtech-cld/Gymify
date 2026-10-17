package backend.gymify.workout.service;

import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.exercise.repository.ExerciseRepository;
import backend.gymify.gym.repository.GymRepository;
import backend.gymify.user.repository.UserRepository;
import backend.gymify.workout.dto.*;
import backend.gymify.workout.entity.*;
import backend.gymify.workout.entity.WorkoutPlan.Difficulty;
import backend.gymify.workout.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {

    private final WorkoutPlanRepository planRepository;
    private final WorkoutAssignmentRepository assignmentRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    public WorkoutService(WorkoutPlanRepository planRepository, WorkoutAssignmentRepository assignmentRepository,
                          ExerciseRepository exerciseRepository, UserRepository userRepository, GymRepository gymRepository) {
        this.planRepository = planRepository;
        this.assignmentRepository = assignmentRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
    }

    @Transactional(readOnly = true)
    public List<WorkoutPlanResponse> getTrainerPlans(UUID trainerId) {
        return planRepository.findByTrainerId(trainerId).stream().map(WorkoutPlanResponse::summary).toList();
    }

    @Transactional(readOnly = true)
    public WorkoutPlanResponse getPlanById(UUID planId) {
        var plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlan", "id", planId));
        return WorkoutPlanResponse.from(plan);
    }

    @Transactional
    public WorkoutPlanResponse createPlan(UUID trainerId, CreateWorkoutPlanRequest request) {
        var trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", trainerId));

        WorkoutPlan plan = new WorkoutPlan();
        plan.setTrainer(trainer);
        plan.setName(request.name());
        plan.setDescription(request.description());
        plan.setGoal(request.goal());
        plan.setDurationWeeks(request.durationWeeks());
        plan.setIsTemplate(request.isTemplate());
        if (request.difficulty() != null) plan.setDifficulty(Difficulty.valueOf(request.difficulty()));

        plan = planRepository.save(plan);

        if (request.days() != null) {
            for (var dayReq : request.days()) {
                WorkoutDay day = new WorkoutDay();
                day.setPlan(plan);
                day.setDayOfWeek((short) dayReq.dayOfWeek());
                day.setName(dayReq.name());
                day.setIsRestDay(dayReq.isRestDay());
                day.setNotes(dayReq.notes());
                plan.getWorkoutDays().add(day);

                if (dayReq.exercises() != null) {
                    for (var exReq : dayReq.exercises()) {
                        var exercise = exerciseRepository.findById(exReq.exerciseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exReq.exerciseId()));
                        WorkoutExercise we = new WorkoutExercise();
                        we.setWorkoutDay(day);
                        we.setExercise(exercise);
                        we.setDisplayOrder(exReq.displayOrder());
                        we.setSets(exReq.sets());
                        we.setReps(exReq.reps());
                        we.setDurationSeconds(exReq.durationSeconds());
                        we.setRestSeconds(exReq.restSeconds());
                        we.setWeightKg(exReq.weightKg());
                        we.setNotes(exReq.notes());
                        day.getExercises().add(we);
                    }
                }
            }
            plan = planRepository.save(plan);
        }
        return WorkoutPlanResponse.from(plan);
    }

    @Transactional
    public WorkoutPlanResponse publishPlan(UUID planId, UUID trainerId) {
        var plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlan", "id", planId));
        plan.setIsPublished(true);
        return WorkoutPlanResponse.from(planRepository.save(plan));
    }

    @Transactional
    public WorkoutAssignmentResponse assignPlan(UUID trainerId, AssignWorkoutRequest request) {
        var plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlan", "id", request.planId()));
        var customer = userRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.customerId()));
        var trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", trainerId));

        WorkoutAssignment assignment = new WorkoutAssignment();
        assignment.setPlan(plan);
        assignment.setCustomer(customer);
        assignment.setTrainer(trainer);
        assignment.setStartDate(request.startDate());
        assignment.setEndDate(request.endDate());
        assignment.setNotes(request.notes());
        if (request.gymId() != null) {
            assignment.setGym(gymRepository.findById(request.gymId()).orElse(null));
        }

        return WorkoutAssignmentResponse.from(assignmentRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public List<WorkoutAssignmentResponse> getCustomerAssignments(UUID customerId) {
        return assignmentRepository.findByCustomerId(customerId).stream()
                .map(WorkoutAssignmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<WorkoutAssignmentResponse> getTrainerAssignments(UUID trainerId) {
        return assignmentRepository.findByTrainerId(trainerId, null).stream()
                .map(WorkoutAssignmentResponse::from).toList();
    }
}
