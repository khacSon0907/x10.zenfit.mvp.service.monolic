package x10.zenfit.mvp.service.service.categorySerrvice.getCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.ICategoryRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;
import x10.zenfit.mvp.service.service.categorySerrvice.CategoryResponseMapper;
import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryResp;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.category.CategoryError;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCategoryBySlugImpl implements IGetCategoryBySlugUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public CreateCategoryResp getBySlug(String slug) {
        CategoryEntity category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));

        return toTree(category, new HashSet<>());
    }

    private CreateCategoryResp toTree(CategoryEntity category, Set<String> visited) {
        if (!visited.add(category.getId())) {
            throw new BusinessException(CategoryError.CATEGORY_CYCLE_DETECTED);
        }

        List<CreateCategoryResp> children = categoryRepository.findByParentId(category.getId())
                .stream()
                .sorted(categoryComparator())
                .map(child -> toTree(child, new HashSet<>(visited)))
                .collect(Collectors.toList());

        return CategoryResponseMapper.toCreateResp(category, children);
    }

    private Comparator<CategoryEntity> categoryComparator() {
        return Comparator
                .comparing((CategoryEntity category) -> Objects.requireNonNullElse(category.getSortOrder(), 0))
                .thenComparing(CategoryEntity::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(CategoryEntity::getId, Comparator.nullsLast(String::compareTo));
    }
}

