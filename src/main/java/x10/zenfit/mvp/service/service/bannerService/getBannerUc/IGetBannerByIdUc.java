package x10.zenfit.mvp.service.service.bannerService.getBannerUc;

import x10.zenfit.mvp.service.service.bannerService.createBannerUc.CreateBannerResp;

public interface IGetBannerByIdUc {
    CreateBannerResp getById(String id);
}
