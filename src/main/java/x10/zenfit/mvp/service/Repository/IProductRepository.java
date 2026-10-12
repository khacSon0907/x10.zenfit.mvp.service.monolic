package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    List<ProductEntity> findAll();
    List<ProductEntity> findAllByCursor(java.time.LocalDateTime cursorCreatedAt, String cursorId, int limit);
    ProductEntity save(ProductEntity product);
    Optional<ProductEntity> findById(String id);
    Optional<ProductEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    void deleteById(String id);
    List<ProductEntity> findByCategoryId(String categoryId);
}
