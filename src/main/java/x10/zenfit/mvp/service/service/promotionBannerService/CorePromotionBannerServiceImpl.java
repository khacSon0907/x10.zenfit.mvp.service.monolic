package x10.zenfit.mvp.service.service.promotionBannerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.promotionBannerService.createPromotionBannerUc.CreatePromotionBannerReq;
import x10.zenfit.mvp.service.service.promotionBannerService.createPromotionBannerUc.ICreatePromotionBannerUc;
import x10.zenfit.mvp.service.service.promotionBannerService.deletePromotionBannerUc.IDeletePromotionBannerUc;
import x10.zenfit.mvp.service.service.promotionBannerService.getPromotionBannerUc.IGetPromotionBannerByIdUc;
import x10.zenfit.mvp.service.service.promotionBannerService.getPromotionBannerUc.IGetPromotionBannersUc;
import x10.zenfit.mvp.service.service.promotionBannerService.updatePromotionBannerUc.IUpdatePromotionBannerUc;
import x10.zenfit.mvp.service.service.promotionBannerService.updatePromotionBannerUc.UpdatePromotionBannerReq;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CorePromotionBannerServiceImpl implements ICorePromotionBannerService {

    private final ICreatePromotionBannerUc createPromotionBannerUc;
    private final IGetPromotionBannersUc getPromotionBannersUc;
    private final IGetPromotionBannerByIdUc getPromotionBannerByIdUc;
    private final IUpdatePromotionBannerUc updatePromotionBannerUc;
    private final IDeletePromotionBannerUc deletePromotionBannerUc;

    @Override
    public PromotionBannerResp createPromotionBanner(CreatePromotionBannerReq req) {
        return createPromotionBannerUc.createPromotionBanner(req);
    }

    @Override
    public List<PromotionBannerResp> getAll() {
        return getPromotionBannersUc.getAll();
    }

    @Override
    public List<PromotionBannerResp> getAllActive() {
        return getPromotionBannersUc.getAllActive();
    }

    @Override
    public List<PromotionBannerResp> getCurrentlyVisible() {
        return getPromotionBannersUc.getCurrentlyVisible();
    }

    @Override
    public PromotionBannerResp getById(String id) {
        return getPromotionBannerByIdUc.getById(id);
    }

    @Override
    public PromotionBannerResp updatePromotionBanner(UpdatePromotionBannerReq req) {
        return updatePromotionBannerUc.updatePromotionBanner(req);
    }

    @Override
    public void deletePromotionBanner(String id) {
        deletePromotionBannerUc.deletePromotionBanner(id);
    }
}
