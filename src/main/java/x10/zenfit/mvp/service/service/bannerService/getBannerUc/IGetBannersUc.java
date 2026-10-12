package x10.zenfit.mvp.service.service.bannerService.getBannerUc;

import x10.zenfit.mvp.service.service.bannerService.createBannerUc.CreateBannerResp;

import java.util.List;

public interface IGetBannersUc {
    List<CreateBannerResp> getAll();
    List<CreateBannerResp> getAllActive();
}
