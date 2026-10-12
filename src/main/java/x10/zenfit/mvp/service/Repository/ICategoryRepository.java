package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    CategoryEntity save(CategoryEntity category);
    Optional<CategoryEntity> findBySlug(String slug);
    Optional<CategoryEntity> findByName(String name);
    Optional<CategoryEntity> findById(String id);
    List<CategoryEntity> findAll();
    List<CategoryEntity> findByParentId(String parentId);
    boolean existsByParentId(String parentId);
    void deleteById(String id);

    // dùng để check duplicate không phân biệt hoa thường
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

}
