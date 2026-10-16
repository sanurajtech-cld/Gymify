package backend.gymify.exercise.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.exercise.dto.*;
import backend.gymify.exercise.entity.*;
import backend.gymify.exercise.entity.Exercise.Difficulty;
import backend.gymify.exercise.entity.Exercise.ExerciseType;
import backend.gymify.exercise.repository.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseCategoryRepository categoryRepository;
    private final MuscleGroupRepository muscleGroupRepository;
    private final EquipmentRepository equipmentRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseCategoryRepository categoryRepository,
                           MuscleGroupRepository muscleGroupRepository, EquipmentRepository equipmentRepository) {
        this.exerciseRepository = exerciseRepository;
        this.categoryRepository = categoryRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<ExerciseResponse> getAllExercises(Pageable pageable) {
        var page = exerciseRepository.findByIsActiveTrue(pageable).map(ExerciseResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public ExerciseResponse getExerciseById(UUID id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", id));
        return ExerciseResponse.from(exercise);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExerciseResponse> searchExercises(String query, Pageable pageable) {
        var page = exerciseRepository.searchExercises(query, pageable).map(ExerciseResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExerciseResponse> getByCategory(UUID categoryId, Pageable pageable) {
        var page = exerciseRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable).map(ExerciseResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExerciseResponse> getByMuscleGroup(UUID muscleGroupId, Pageable pageable) {
        var page = exerciseRepository.findByMuscleGroupId(muscleGroupId, pageable).map(ExerciseResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public List<ExerciseCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(ExerciseCategoryResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MuscleGroupResponse> getAllMuscleGroups() {
        return muscleGroupRepository.findAll().stream().map(MuscleGroupResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<EquipmentResponse> getAllEquipment() {
        return equipmentRepository.findAll().stream().map(EquipmentResponse::from).toList();
    }

    @Transactional
    public ExerciseResponse createExercise(CreateExerciseRequest request, UUID createdBy) {
        Exercise exercise = new Exercise();
        exercise.setName(request.name());
        exercise.setDescription(request.description());
        exercise.setInstructions(request.instructions());
        exercise.setImageUrl(request.imageUrl());
        exercise.setVideoUrl(request.videoUrl());
        exercise.setThumbnailUrl(request.thumbnailUrl());
        exercise.setCaloriesPerMinute(request.caloriesPerMinute());

        if (request.categoryId() != null) {
            exercise.setCategory(categoryRepository.findById(request.categoryId()).orElse(null));
        }
        if (request.difficulty() != null) {
            exercise.setDifficulty(Difficulty.valueOf(request.difficulty()));
        }
        if (request.exerciseType() != null) {
            exercise.setExerciseType(ExerciseType.valueOf(request.exerciseType()));
        }
        if (request.muscleGroupIds() != null) {
            exercise.setMuscleGroups(new HashSet<>(muscleGroupRepository.findAllById(request.muscleGroupIds())));
        }
        if (request.equipmentIds() != null) {
            exercise.setEquipment(new HashSet<>(equipmentRepository.findAllById(request.equipmentIds())));
        }

        exercise = exerciseRepository.save(exercise);
        return ExerciseResponse.from(exercise);
    }
}
