package x10.zenfit.mvp.service.service.bannerService;

import x10.zenfit.mvp.service.service.bannerService.createBannerUc.CreateBannerReq;
import x10.zenfit.mvp.service.service.bannerService.createBannerUc.CreateBannerResp;
import x10.zenfit.mvp.service.service.bannerService.updateBannerUc.UpdateBannerReq;
import x10.zenfit.mvp.service.service.bannerService.updateBannerUc.UpdateBannerResp;

import java.util.List;

public interface ICoreBannerService {
    CreateBannerResp createBanner(CreateBannerReq req);
    List<CreateBannerResp> getAll();
    List<CreateBannerResp> getAllActive();
    CreateBannerResp getById(String id);
    UpdateBannerResp updateBanner(UpdateBannerReq req);
    void deleteBanner(String bannerId);
}
