package x10.zenfit.mvp.service.infrastructure.product.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {
    Optional<ProductDocument> findBySlug(String slug);
    boolean existsBySlug(String slug);
    java.util.List<ProductDocument> findByNameContainingIgnoreCase(String name);
    java.util.List<ProductDocument> findByCategoryId(String categoryId);
}
