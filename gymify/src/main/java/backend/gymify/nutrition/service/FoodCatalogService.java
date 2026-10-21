package backend.gymify.nutrition.service;

import backend.gymify.common.dto.PageResponse;
import backend.gymify.nutrition.dto.response.FoodCatalogResponse;
import backend.gymify.nutrition.repository.FoodCatalogRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FoodCatalogService {

    private final FoodCatalogRepository foodCatalogRepository;

    public FoodCatalogService(FoodCatalogRepository foodCatalogRepository) {
        this.foodCatalogRepository = foodCatalogRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<FoodCatalogResponse> search(String query, Pageable pageable) {
        var page = foodCatalogRepository
                .findByIsActiveTrueAndCanonicalNameContainingIgnoreCase(query, pageable)
                .map(FoodCatalogResponse::from);
        return PageResponse.from(page);
    }
}
