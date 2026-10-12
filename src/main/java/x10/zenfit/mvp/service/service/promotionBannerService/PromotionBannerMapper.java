package x10.zenfit.mvp.service.service.promotionBannerService;

import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;

public class PromotionBannerMapper {

    public static PromotionBannerResp toResp(PromotionBannerEntity entity) {
        if (entity == null) return null;
        return PromotionBannerResp.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .active(entity.isActive())
                .sortOrder(entity.getSortOrder())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
