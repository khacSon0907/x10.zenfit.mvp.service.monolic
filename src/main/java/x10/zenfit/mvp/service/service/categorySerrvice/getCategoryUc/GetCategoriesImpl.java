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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCategoriesImpl implements IGetCategoriesUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public List<CreateCategoryResp> getAll() {
        List<CategoryEntity> list = categoryRepository.findAll();
        Map<String, List<CategoryEntity>> childrenByParentId = list.stream()
                .filter(category -> category.getParentId() != null)
                .collect(Collectors.groupingBy(CategoryEntity::getParentId));

        return list.stream()
                .filter(category -> category.getParentId() == null)
                .sorted(categoryComparator())
                .map(category -> toTree(category, childrenByParentId, new HashSet<>()))
                .collect(Collectors.toList());
    }

    private CreateCategoryResp toTree(
            CategoryEntity category,
            Map<String, List<CategoryEntity>> childrenByParentId,
            Set<String> visited
    ) {
        if (!visited.add(category.getId())) {
            throw new BusinessException(CategoryError.CATEGORY_CYCLE_DETECTED);
        }

        List<CreateCategoryResp> children = childrenByParentId
                .getOrDefault(category.getId(), List.of())
                .stream()
                .sorted(categoryComparator())
                .map(child -> toTree(child, childrenByParentId, new HashSet<>(visited)))
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

