package x10.zenfit.mvp.service.infrastructure.category.adapter;

import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;
import x10.zenfit.mvp.service.infrastructure.category.db.mongodb.CategoryDocument;

public class CategoryMapper {

    public static CategoryEntity toEntity(CategoryDocument doc) {
        if (doc == null) return null;
        return CategoryEntity.builder()
                .id(doc.getId())
                .name(doc.getName())
                .slug(doc.getSlug())
                .description(doc.getDescription())
                .active(doc.isActive())
                .bannerUrl(doc.getBannerUrl())
                .parentId(doc.getParentId())
                .sortOrder(doc.getSortOrder())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static CategoryDocument toDocument(CategoryEntity entity) {
        if (entity == null) return null;
        return CategoryDocument.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .active(entity.isActive())
                .bannerUrl(entity.getBannerUrl())
                .parentId(entity.getParentId())
                .sortOrder(entity.getSortOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

