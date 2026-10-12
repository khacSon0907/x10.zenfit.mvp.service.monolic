package x10.zenfit.mvp.service.infrastructure.product.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.infrastructure.product.adapter.mapper.ProductMapper;
import x10.zenfit.mvp.service.infrastructure.product.db.mongodb.ProductDocument;
import x10.zenfit.mvp.service.infrastructure.product.db.mongodb.ProductMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {

    private final ProductMongoRepository productMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductEntity save(ProductEntity product) {
        ProductDocument document = ProductMapper.toDocument(product);
        ProductDocument savedDocument = productMongoRepository.save(document);
        return ProductMapper.toEntity(savedDocument);
    }

    @Override
    public List<ProductEntity> findAll() {
        return productMongoRepository.findAll().stream()
                .map(ProductMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByCursor(LocalDateTime cursorCreatedAt, String cursorId, int limit) {
        Query query = new Query()
                .with(Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("_id")
                ))
                .limit(limit);

        if (cursorCreatedAt != null && cursorId != null && !cursorId.isBlank()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("createdAt").lt(cursorCreatedAt),
                    new Criteria().andOperator(
                            Criteria.where("createdAt").is(cursorCreatedAt),
                            Criteria.where("_id").lt(cursorId)
                    )
            ));
        }

        return mongoTemplate.find(query, ProductDocument.class).stream()
                .map(ProductMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductEntity> findById(String id) {
        return productMongoRepository.findById(id)
                .map(ProductMapper::toEntity);
    }

    @Override
    public Optional<ProductEntity> findBySlug(String slug) {
        return productMongoRepository.findBySlug(slug)
                .map(ProductMapper::toEntity);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return productMongoRepository.existsBySlug(slug);
    }

    @Override
    public List<ProductEntity> findByNameContainingIgnoreCase(String name) {
        return productMongoRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ProductMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        productMongoRepository.deleteById(id);
    }

    @Override
    public List<ProductEntity> findByCategoryId(String categoryId) {
        return productMongoRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::toEntity)
                .collect(Collectors.toList());
    }
}
