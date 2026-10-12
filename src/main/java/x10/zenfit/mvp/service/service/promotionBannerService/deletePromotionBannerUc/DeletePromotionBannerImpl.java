package x10.zenfit.mvp.service.service.promotionBannerService.deletePromotionBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPromotionBannerRepository;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.promotionBanner.PromotionBannerError;

@Service
@RequiredArgsConstructor
public class DeletePromotionBannerImpl implements IDeletePromotionBannerUc {

    private final IPromotionBannerRepository promotionBannerRepository;

    @Override
    public void deletePromotionBanner(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Promotion banner ID là bắt buộc");
        }

        promotionBannerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PromotionBannerError.PROMOTION_BANNER_NOT_FOUND));

        promotionBannerRepository.deleteById(id);
    }
}
