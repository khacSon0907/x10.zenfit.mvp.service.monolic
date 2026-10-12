package x10.zenfit.mvp.service.service.promotionBannerService;

import x10.zenfit.mvp.service.service.promotionBannerService.createPromotionBannerUc.CreatePromotionBannerReq;
import x10.zenfit.mvp.service.service.promotionBannerService.updatePromotionBannerUc.UpdatePromotionBannerReq;

import java.util.List;

public interface ICorePromotionBannerService {
    PromotionBannerResp createPromotionBanner(CreatePromotionBannerReq req);
    List<PromotionBannerResp> getAll();
    List<PromotionBannerResp> getAllActive();
    List<PromotionBannerResp> getCurrentlyVisible();
    PromotionBannerResp getById(String id);
    PromotionBannerResp updatePromotionBanner(UpdatePromotionBannerReq req);
    void deletePromotionBanner(String id);
}
