package x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc;

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

@Service
@RequiredArgsConstructor
public class UpdateCategoryImpl implements IUpdateCategoryUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public UpdateCategoryResp updateCategory(UpdateCategoryReq req) {

        validateRequest(req);

        // Find existing category
        CategoryEntity entity = categoryRepository.findById(req.getId())
                .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));

        // Check if name changed and if new name already exists
        if (!entity.getName().equalsIgnoreCase(req.getName())) {
            categoryRepository.findByNameIgnoreCase(req.getName())
                    .ifPresent(category -> {
                        throw new BusinessException(CategoryError.CATEGORY_EXISTS);
                    });
        }

        // Update name and slug if name changed
        if (!entity.getName().equalsIgnoreCase(req.getName())) {
            entity.setName(req.getName().trim());
            String baseSlug = generateSlug(req.getName());
            String finalSlug = generateUniqueSlug(baseSlug, req.getId());
            entity.setSlug(finalSlug);
        }

        // Update other fields
        if (req.getDescription() != null) {
            entity.setDescription(req.getDescription());
        }

        if (req.getBannerUrl() != null) {
            entity.setBannerUrl(req.getBannerUrl());
        }

        if (req.getActive() != null) {
            entity.setActive(req.getActive());
        }

        if (req.getParentId() != null) {
            String parentId = normalizeParentId(req.getParentId());
            validateParent(entity.getId(), parentId);
            entity.setParentId(parentId);
        }

        if (req.getSortOrder() != null) {
            entity.setSortOrder(req.getSortOrder());
        }

        entity.setUpdatedAt(Instant.now());

        CategoryEntity saved = categoryRepository.save(entity);

        return CategoryResponseMapper.toUpdateResp(saved);
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
     * Ensure slug uniqueness (excluding current category)
     *
     * Example:
     * ao-thun
     * ao-thun-1
     * ao-thun-2
     */
    private String generateUniqueSlug(String baseSlug, String excludeId) {

        String candidate = baseSlug;
        int suffix = 1;

        while (categoryRepository.findBySlug(candidate).isPresent()) {
            CategoryEntity existing = categoryRepository.findBySlug(candidate).get();
            // If it's the same category we're updating, use this slug
            if (existing.getId().equals(excludeId)) {
                return candidate;
            }
            candidate = baseSlug + "-" + suffix;
            suffix++;
        }

        return candidate;
    }

    private void validateRequest(UpdateCategoryReq req) {

        if (req == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (req.getId() == null || req.getId().isBlank()) {
            throw new IllegalArgumentException("Category ID is required");
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

    private void validateParent(String categoryId, String parentId) {
        if (parentId == null) {
            return;
        }

        if (categoryId.equals(parentId)) {
            throw new BusinessException(CategoryError.CATEGORY_INVALID_PARENT);
        }

        Set<String> visited = new HashSet<>();
        String currentParentId = parentId;

        while (currentParentId != null) {
            if (categoryId.equals(currentParentId)) {
                throw new BusinessException(CategoryError.CATEGORY_CYCLE_DETECTED);
            }

            if (!visited.add(currentParentId)) {
                throw new BusinessException(CategoryError.CATEGORY_CYCLE_DETECTED);
            }

            CategoryEntity parent = categoryRepository.findById(currentParentId)
                    .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_INVALID_PARENT));
            currentParentId = normalizeParentId(parent.getParentId());
        }
    }
}

