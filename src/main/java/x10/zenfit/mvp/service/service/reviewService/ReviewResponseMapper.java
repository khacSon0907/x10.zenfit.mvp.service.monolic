package x10.zenfit.mvp.service.service.reviewService;

import x10.zenfit.mvp.service.common.domain.entities.review.ReviewEntity;

import java.util.Collections;

public class ReviewResponseMapper {

    public static ReviewResponse toResponse(ReviewEntity entity) {
        if (entity == null) {
            return null;
        }

        return ReviewResponse.builder()
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
