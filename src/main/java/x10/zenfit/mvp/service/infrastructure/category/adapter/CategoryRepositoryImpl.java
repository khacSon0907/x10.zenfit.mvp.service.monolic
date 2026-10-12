package x10.zenfit.mvp.service.infrastructure.category.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.ICategoryRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;
import x10.zenfit.mvp.service.infrastructure.category.db.mongodb.CategoryDocument;
import x10.zenfit.mvp.service.infrastructure.category.db.mongodb.CategoryMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements ICategoryRepository {

    private final CategoryMongoRepository categoryMongoRepository;

    @Override
    public CategoryEntity save(CategoryEntity category) {
        CategoryDocument doc = CategoryMapper.toDocument(category);
        CategoryDocument saved = categoryMongoRepository.save(doc);
        return CategoryMapper.toEntity(saved);
    }

    @Override
    public Optional<CategoryEntity> findBySlug(String slug) {
        return categoryMongoRepository.findBySlug(slug).map(CategoryMapper::toEntity);
    }

    @Override
    public Optional<CategoryEntity> findByName(String name) {
        return categoryMongoRepository.findByName(name).map(CategoryMapper::toEntity);
    }

    @Override
    public Optional<CategoryEntity> findById(String id) {
        return categoryMongoRepository.findById(id).map(CategoryMapper::toEntity);
    }

    @Override
    public List<CategoryEntity> findAll() {
        return categoryMongoRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(CategoryMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryEntity> findByParentId(String parentId) {
        return categoryMongoRepository.findByParentIdOrderBySortOrderAsc(parentId)
                .stream()
                .map(CategoryMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByParentId(String parentId) {
        return categoryMongoRepository.existsByParentId(parentId);
    }
    @Override
    public Optional<CategoryEntity> findByNameIgnoreCase(String name) {

        return categoryMongoRepository
                .findByNameIgnoreCase(name)
                .map(CategoryMapper::toEntity);
    }

    @Override
    public void deleteById(String id) {
        categoryMongoRepository.deleteById(id);
    }
}
