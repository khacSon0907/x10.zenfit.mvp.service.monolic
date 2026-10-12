package x10.zenfit.mvp.service.infrastructure.category.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryMongoRepository extends MongoRepository<CategoryDocument, String> {
    Optional<CategoryDocument> findBySlug(String slug);
    Optional<CategoryDocument> findByName(String name);
    List<CategoryDocument> findAllByOrderBySortOrderAsc();
    List<CategoryDocument> findByParentIdOrderBySortOrderAsc(String parentId);
    boolean existsByParentId(String parentId);


    Optional<CategoryDocument> findByNameIgnoreCase(String name);
}

