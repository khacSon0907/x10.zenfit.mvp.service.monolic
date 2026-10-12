package x10.zenfit.mvp.service.service.promotionBannerService.getPromotionBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPromotionBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerMapper;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerResp;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.promotionBanner.PromotionBannerError;

@Service
@RequiredArgsConstructor
public class GetPromotionBannerByIdImpl implements IGetPromotionBannerByIdUc {

    private final IPromotionBannerRepository promotionBannerRepository;

    @Override
    public PromotionBannerResp getById(String id) {
        PromotionBannerEntity entity = promotionBannerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PromotionBannerError.PROMOTION_BANNER_NOT_FOUND));
        return PromotionBannerMapper.toResp(entity);
    }
}
