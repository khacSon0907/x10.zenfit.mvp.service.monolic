package x10.zenfit.mvp.service.infrastructure.review.adapter;

import x10.zenfit.mvp.service.common.domain.entities.review.ReviewEntity;
import x10.zenfit.mvp.service.infrastructure.review.db.mongodb.ReviewDocument;

import java.util.Collections;

public class ReviewMapper {

    public static ReviewEntity toEntity(ReviewDocument document) {
        if (document == null) {
            return null;
        }

        return ReviewEntity.builder()
                .id(document.getId())
                .productId(document.getProductId())
                .userId(document.getUserId())
                .rating(document.getRating())
                .comment(document.getComment())
                .images(document.getImages() != null ? document.getImages() : Collections.emptyList())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static ReviewDocument toDocument(ReviewEntity entity) {
        if (entity == null) {
            return null;
        }

        return ReviewDocument.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .userId(entity.getUserId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .images(entity.getImages() != null ? entity.getImages() : Collections.emptyList())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
