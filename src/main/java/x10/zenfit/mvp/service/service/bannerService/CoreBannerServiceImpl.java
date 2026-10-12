package x10.zenfit.mvp.service.service.bannerService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.bannerService.createBannerUc.CreateBannerReq;
import x10.zenfit.mvp.service.service.bannerService.createBannerUc.CreateBannerResp;
import x10.zenfit.mvp.service.service.bannerService.createBannerUc.ICreateBannerUc;
import x10.zenfit.mvp.service.service.bannerService.deleteBannerUc.IDeleteBannerUc;
import x10.zenfit.mvp.service.service.bannerService.getBannerUc.IGetBannerByIdUc;
import x10.zenfit.mvp.service.service.bannerService.getBannerUc.IGetBannersUc;
import x10.zenfit.mvp.service.service.bannerService.updateBannerUc.IUpdateBannerUc;
import x10.zenfit.mvp.service.service.bannerService.updateBannerUc.UpdateBannerReq;
import x10.zenfit.mvp.service.service.bannerService.updateBannerUc.UpdateBannerResp;

import java.util.List;

@Service
@AllArgsConstructor
public class CoreBannerServiceImpl implements ICoreBannerService {

    private final ICreateBannerUc createBannerUc;
    private final IGetBannersUc getBannersUc;
    private final IGetBannerByIdUc getBannerByIdUc;
    private final IUpdateBannerUc updateBannerUc;
    private final IDeleteBannerUc deleteBannerUc;

    @Override
    public CreateBannerResp createBanner(CreateBannerReq req) {
        return createBannerUc.createBanner(req);
    }

    @Override
    public List<CreateBannerResp> getAll() {
        return getBannersUc.getAll();
    }

    @Override
    public List<CreateBannerResp> getAllActive() {
        return getBannersUc.getAllActive();
    }

    @Override
    public CreateBannerResp getById(String id) {
        return getBannerByIdUc.getById(id);
    }

    @Override
    public UpdateBannerResp updateBanner(UpdateBannerReq req) {
        return updateBannerUc.updateBanner(req);
    }

    @Override
    public void deleteBanner(String bannerId) {
        deleteBannerUc.deleteBanner(bannerId);
    }
}
