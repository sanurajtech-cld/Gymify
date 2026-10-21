package backend.gymify.nutrition.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.common.exception.BadRequestException;
import backend.gymify.common.exception.ResourceNotFoundException;
import backend.gymify.nutrition.dto.request.ConfirmMealItemRequest;
import backend.gymify.nutrition.dto.request.ConfirmMealRequest;
import backend.gymify.nutrition.dto.response.AiRecognitionResult;
import backend.gymify.nutrition.dto.response.DailyNutritionResponse;
import backend.gymify.nutrition.dto.response.MealAnalysisResponse;
import backend.gymify.nutrition.dto.response.NutritionSummary;
import backend.gymify.nutrition.entity.MealEntry;
import backend.gymify.nutrition.entity.MealEntry.MealStatus;
import backend.gymify.nutrition.entity.MealItem;
import backend.gymify.nutrition.exception.FoodAnalysisException;
import backend.gymify.nutrition.integration.ai.VisionAiClient;
import backend.gymify.nutrition.repository.FoodCatalogRepository;
import backend.gymify.nutrition.repository.MealEntryRepository;
import backend.gymify.nutrition.repository.MealItemRepository;
import backend.gymify.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class MealService {

    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealEntryRepository mealEntryRepository;
    private final MealItemRepository mealItemRepository;
    private final FoodCatalogRepository foodCatalogRepository;
    private final UserRepository userRepository;
    private final VisionAiClient visionAiClient;
    private final FoodMatchingService foodMatchingService;
    private final NutritionCalculationService nutritionCalculationService;

    public MealService(MealEntryRepository mealEntryRepository,
                       MealItemRepository mealItemRepository,
                       FoodCatalogRepository foodCatalogRepository,
                       UserRepository userRepository,
                       VisionAiClient visionAiClient,
                       FoodMatchingService foodMatchingService,
                       NutritionCalculationService nutritionCalculationService) {
        this.mealEntryRepository = mealEntryRepository;
        this.mealItemRepository = mealItemRepository;
        this.foodCatalogRepository = foodCatalogRepository;
        this.userRepository = userRepository;
        this.visionAiClient = visionAiClient;
        this.foodMatchingService = foodMatchingService;
        this.nutritionCalculationService = nutritionCalculationService;
    }

    @Transactional
    public MealAnalysisResponse analyzeImage(UUID userId, MultipartFile image) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        byte[] imageBytes = readImageBytes(image);
        String sha256 = computeSha256(imageBytes);

        if (mealEntryRepository.existsByImageSha256AndUser_Id(sha256, userId)) {
            throw new BadRequestException("This image has already been analyzed. Please use the existing meal entry.");
        }

        MealEntry entry = new MealEntry();
        entry.setUser(user);
        entry.setStatus(MealStatus.PROCESSING);
        entry.setImageSha256(sha256);
        entry = mealEntryRepository.save(entry);

        try {
            AiRecognitionResult result = visionAiClient.recognizeFood(imageBytes, image.getContentType());
            entry.setModelName(result.modelName());
            entry.setPromptVersion(result.promptVersion());

            for (var detected : result.foodItems()) {
                MealItem item = new MealItem();
                item.setMealEntry(entry);
                item.setDetectedFoodName(detected.detectedName());
                item.setEstimatedWeightG(detected.estimatedWeightGrams());
                item.setConfidenceScore(resolveConfidenceScore(detected.confidence()));

                foodMatchingService.matchByName(detected.detectedName()).ifPresent(food -> {
                    item.setFood(food);
                    BigDecimal weight = detected.estimatedWeightGrams() != null
                            ? detected.estimatedWeightGrams() : new BigDecimal("100");
                    nutritionCalculationService.calculateAndApply(item, food, weight);
                });

                entry.getItems().add(item);
            }

            entry.setStatus(MealStatus.NEEDS_CONFIRMATION);
            entry = mealEntryRepository.save(entry);
            log.info("Meal analysis completed for user {} — {} items detected", userId, entry.getItems().size());

        } catch (FoodAnalysisException e) {
            entry.setStatus(MealStatus.FAILED);
            mealEntryRepository.save(entry);
            throw e;
        }

        return MealAnalysisResponse.from(entry);
    }

    @Transactional(readOnly = true)
    public MealAnalysisResponse getAnalysis(UUID mealId, UUID userId) {
        MealEntry entry = mealEntryRepository.findByIdAndUser_Id(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("MealEntry", "id", mealId));
        return MealAnalysisResponse.from(entry);
    }

    @Transactional
    public MealAnalysisResponse confirmMeal(UUID mealId, UUID userId, ConfirmMealRequest request) {
        MealEntry entry = mealEntryRepository.findByIdAndUser_Id(mealId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("MealEntry", "id", mealId));

        if (entry.getStatus() == MealStatus.CONFIRMED) {
            throw new BadRequestException("Meal is already confirmed.");
        }

        for (ConfirmMealItemRequest itemReq : request.items()) {
            MealItem item = entry.getItems().stream()
                    .filter(i -> i.getId().equals(itemReq.itemId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("MealItem", "id", itemReq.itemId()));

            item.setDetectedFoodName(itemReq.foodName());
            item.setConfirmedWeightG(itemReq.confirmedWeightG());

            var food = itemReq.foodCatalogId() != null
                    ? foodCatalogRepository.findById(itemReq.foodCatalogId()).orElse(item.getFood())
                    : foodMatchingService.matchByName(itemReq.foodName()).orElse(item.getFood());

            if (food != null) {
                item.setFood(food);
                nutritionCalculationService.calculateAndApply(item, food, itemReq.confirmedWeightG());
            }
        }

        entry.setStatus(MealStatus.CONFIRMED);
        entry.setConfirmedAt(Instant.now());
        entry = mealEntryRepository.save(entry);
        log.info("Meal {} confirmed by user {}", mealId, userId);
        return MealAnalysisResponse.from(entry);
    }

    @Transactional(readOnly = true)
    public PageResponse<MealAnalysisResponse> getMealHistory(UUID userId, Pageable pageable) {
        var page = mealEntryRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable)
                .map(MealAnalysisResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public DailyNutritionResponse getDailyNutrition(UUID userId, LocalDate date) {
        Instant from = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<MealAnalysisResponse> meals = mealEntryRepository
                .findByUserAndStatusAndDateRange(userId, MealStatus.CONFIRMED, from, to)
                .stream().map(MealAnalysisResponse::from).toList();

        Object[] raw = mealItemRepository.sumNutrientsForUserAndDateRange(userId, from, to);
        NutritionSummary totals = NutritionSummary.fromRaw(raw);

        return new DailyNutritionResponse(date, totals, meals);
    }

    private byte[] readImageBytes(MultipartFile image) {
        try {
            return image.getBytes();
        } catch (IOException e) {
            throw new FoodAnalysisException("Failed to read uploaded image.");
        }
    }

    private String computeSha256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private BigDecimal resolveConfidenceScore(String confidence) {
        return switch (confidence == null ? "low" : confidence.toLowerCase()) {
            case "high"   -> new BigDecimal("0.90");
            case "medium" -> new BigDecimal("0.65");
            default       -> new BigDecimal("0.40");
        };
    }
}
