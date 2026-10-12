package x10.zenfit.mvp.service.infrastructure.promotionBanner.adapter;

import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;
import x10.zenfit.mvp.service.infrastructure.promotionBanner.db.mongodb.PromotionBannerDocument;

public class PromotionBannerMapper {

    public static PromotionBannerEntity toEntity(PromotionBannerDocument document) {
        if (document == null) return null;
        return PromotionBannerEntity.builder()
                .id(document.getId())
                .title(document.getTitle())
                .active(document.isActive())
                .sortOrder(document.getSortOrder())
                .startDate(document.getStartDate())
                .endDate(document.getEndDate())
                .build();
    }

    public static PromotionBannerDocument toDocument(PromotionBannerEntity entity) {
        if (entity == null) return null;
        return PromotionBannerDocument.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .active(entity.isActive())
                .sortOrder(entity.getSortOrder())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
