package x10.zenfit.mvp.service.service.promotionBannerService.getPromotionBannerUc;

import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerResp;

import java.util.List;

public interface IGetPromotionBannersUc {
    List<PromotionBannerResp> getAll();
    List<PromotionBannerResp> getAllActive();
    List<PromotionBannerResp> getCurrentlyVisible();
}
