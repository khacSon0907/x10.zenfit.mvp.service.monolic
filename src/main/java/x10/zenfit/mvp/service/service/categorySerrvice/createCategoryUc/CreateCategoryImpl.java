package x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.ICategoryRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;
import x10.zenfit.mvp.service.service.categorySerrvice.CategoryResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.category.CategoryError;

import java.text.Normalizer;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCategoryImpl implements ICreateCategoryUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public CreateCategoryResp createCategory(CreateCategoryReq req) {

        validateRequest(req);

        // check duplicate name (ignore case)
        categoryRepository.findByNameIgnoreCase(req.getName())
                .ifPresent(category -> {
                    throw new BusinessException(CategoryError.CATEGORY_EXISTS);
                });

        // generate slug
        String baseSlug = generateSlug(req.getName());

        // ensure unique slug
        String finalSlug = generateUniqueSlug(baseSlug);
        String parentId = normalizeParentId(req.getParentId());
        validateParentExistsAndHasNoCycle(parentId);
        Instant now = Instant.now();

        CategoryEntity entity = CategoryEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(req.getName().trim())
                .slug(finalSlug)
                .description(req.getDescription())
                .bannerUrl(req.getBannerUrl())
                .active(req.getActive() == null || req.getActive())
                .parentId(parentId)
                .sortOrder(req.getSortOrder() == null ? 0 : req.getSortOrder())
                .createdAt(now)
                .updatedAt(now)
                .build();

        CategoryEntity saved = categoryRepository.save(entity);

        return CategoryResponseMapper.toCreateResp(saved);
    }

    /**
     * Generate SEO friendly slug
     *
     * Example:
     * "Áo Thun Nam" -> "ao-thun-nam"
     */
    private String generateSlug(String input) {

        if (input == null || input.isBlank()) {
            return "";
        }

        // lowercase + trim
        String slug = input.toLowerCase(Locale.ROOT).trim();

        // remove vietnamese accents
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // replace đ
        slug = slug.replace("đ", "d");

        // remove special characters
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // replace spaces with -
        slug = slug.replaceAll("\\s+", "-");

        // remove duplicate -
        slug = slug.replaceAll("-+", "-");

        // remove leading/trailing -
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }

    /**
     * Ensure slug uniqueness
     *
     * Example:
     * ao-thun
     * ao-thun-1
     * ao-thun-2
     */
    private String generateUniqueSlug(String baseSlug) {

        String candidate = baseSlug;
        int suffix = 1;

        while (categoryRepository.findBySlug(candidate).isPresent()) {
            candidate = baseSlug + "-" + suffix;
            suffix++;
        }

        return candidate;
    }

    private void validateRequest(CreateCategoryReq req) {

        if (req == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (req.getName() == null || req.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
    }

    private String normalizeParentId(String parentId) {
        if (parentId == null || parentId.isBlank()) {
            return null;
        }
        return parentId.trim();
    }

    private void validateParentExistsAndHasNoCycle(String parentId) {
        if (parentId == null) {
            return;
        }

        Set<String> visited = new HashSet<>();
        String currentParentId = parentId;

        while (currentParentId != null) {
            if (!visited.add(currentParentId)) {
                throw new BusinessException(CategoryError.CATEGORY_CYCLE_DETECTED);
            }

            CategoryEntity parent = categoryRepository.findById(currentParentId)
                    .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_INVALID_PARENT));
            currentParentId = normalizeParentId(parent.getParentId());
        }
    }
}
