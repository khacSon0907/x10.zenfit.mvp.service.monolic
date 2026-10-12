package x10.zenfit.mvp.service.infrastructure.product.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductImageEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;
import x10.zenfit.mvp.service.infrastructure.product.db.mongodb.ColorVariantDocument;
import x10.zenfit.mvp.service.infrastructure.product.db.mongodb.ProductDocument;
import x10.zenfit.mvp.service.infrastructure.product.db.mongodb.ProductImageDocument;
import x10.zenfit.mvp.service.infrastructure.product.db.mongodb.SizeVariantDocument;

import java.util.Collections;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductEntity toEntity(ProductDocument document) {
        if (document == null) {
            return null;
        }

        return ProductEntity.builder()
                .id(document.getId())
                .name(document.getName())
                .slug(document.getSlug())
                .categoryId(document.getCategoryId())
                .description(document.getDescription())
                .price(document.getPrice())
                .salePrice(document.getSalePrice())
                .active(document.getActive())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .colors(document.getColors() != null ? document.getColors().stream().map(ProductMapper::toColorVariantEntity).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public static ProductDocument toDocument(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductDocument.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .categoryId(entity.getCategoryId())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .salePrice(entity.getSalePrice())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .colors(entity.getColors() != null ? entity.getColors().stream().map(ProductMapper::toColorVariantDocument).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    private static ColorVariantEntity toColorVariantEntity(ColorVariantDocument document) {
        if (document == null) return null;
        return ColorVariantEntity.builder()
                .id(document.getId())
                .colorName(document.getColorName())
                .colorCode(document.getColorCode())
                .images(document.getImages() != null ? document.getImages().stream().map(ProductMapper::toProductImageEntity).collect(Collectors.toList()) : Collections.emptyList())
                .sizes(document.getSizes() != null ? document.getSizes().stream().map(ProductMapper::toSizeVariantEntity).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    private static ColorVariantDocument toColorVariantDocument(ColorVariantEntity entity) {
        if (entity == null) return null;
        return ColorVariantDocument.builder()
                .id(entity.getId())
                .colorName(entity.getColorName())
                .colorCode(entity.getColorCode())
                .images(entity.getImages() != null ? entity.getImages().stream().map(ProductMapper::toProductImageDocument).collect(Collectors.toList()) : Collections.emptyList())
                .sizes(entity.getSizes() != null ? entity.getSizes().stream().map(ProductMapper::toSizeVariantDocument).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    private static SizeVariantEntity toSizeVariantEntity(SizeVariantDocument document) {
        if (document == null) return null;
        return SizeVariantEntity.builder()
                .id(document.getId())
                .size(document.getSize())
                .sku(document.getSku())
                .quantity(document.getQuantity())
                .soldQuantity(defaultZero(document.getSoldQuantity()))
                .build();
    }

    private static SizeVariantDocument toSizeVariantDocument(SizeVariantEntity entity) {
        if (entity == null) return null;
        return SizeVariantDocument.builder()
                .id(entity.getId())
                .size(entity.getSize())
                .sku(entity.getSku())
                .quantity(entity.getQuantity())
                .soldQuantity(defaultZero(entity.getSoldQuantity()))
                .build();
    }

    private static Integer defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private static ProductImageEntity toProductImageEntity(ProductImageDocument document) {
        if (document == null) return null;
        return ProductImageEntity.builder()
                .id(document.getId())
                .url(document.getUrl())
                .publicId(document.getPublicId())
                .main(document.getMain())
                .sortOrder(document.getSortOrder())
                .build();
    }

    private static ProductImageDocument toProductImageDocument(ProductImageEntity entity) {
        if (entity == null) return null;
        return ProductImageDocument.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .publicId(entity.getPublicId())
                .main(entity.getMain())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
