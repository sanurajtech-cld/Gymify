package backend.gymify.nutrition.service;

import backend.gymify.nutrition.entity.FoodCatalog;
import backend.gymify.nutrition.repository.FoodCatalogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FoodMatchingService {

    private static final Logger log = LoggerFactory.getLogger(FoodMatchingService.class);

    private final FoodCatalogRepository foodCatalogRepository;

    public FoodMatchingService(FoodCatalogRepository foodCatalogRepository) {
        this.foodCatalogRepository = foodCatalogRepository;
    }

    @Transactional(readOnly = true)
    public Optional<FoodCatalog> matchByName(String detectedName) {
        // 1. Exact canonical name match
        Optional<FoodCatalog> exact = foodCatalogRepository
                .findByCanonicalNameIgnoreCaseAndIsActiveTrue(detectedName.trim());
        if (exact.isPresent()) return exact;

        // 2. Alias / partial match
        List<FoodCatalog> candidates = foodCatalogRepository.searchByNameOrAlias(detectedName.trim());
        if (!candidates.isEmpty()) {
            log.debug("Matched '{}' via alias/partial to '{}'", detectedName, candidates.get(0).getCanonicalName());
            return Optional.of(candidates.get(0));
        }

        log.warn("No food catalog match found for detected name: '{}'", detectedName);
        return Optional.empty();
    }
}
