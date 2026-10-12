package x10.zenfit.mvp.service.service.promotionBannerService.updatePromotionBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPromotionBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerMapper;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerResp;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.promotionBanner.PromotionBannerError;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdatePromotionBannerImpl implements IUpdatePromotionBannerUc {

    private final IPromotionBannerRepository promotionBannerRepository;

    @Override
    public PromotionBannerResp updatePromotionBanner(UpdatePromotionBannerReq req) {
        PromotionBannerEntity existing = promotionBannerRepository.findById(req.getId())
                .orElseThrow(() -> new BusinessException(PromotionBannerError.PROMOTION_BANNER_NOT_FOUND));

        if (req.getTitle() != null && !req.getTitle().isBlank()) {
            existing.setTitle(req.getTitle().trim());
        }
        if (req.getActive() != null) {
            existing.setActive(req.getActive());
        }
        if (req.getSortOrder() != null) {
            existing.setSortOrder(req.getSortOrder());
        }
        if (req.getStartDate() != null) {
            existing.setStartDate(req.getStartDate());
        }
        if (req.getEndDate() != null) {
            existing.setEndDate(req.getEndDate());
        }

        validatePromotionDates(req, existing);

        PromotionBannerEntity saved = promotionBannerRepository.save(existing);
        return PromotionBannerMapper.toResp(saved);
    }

    private void validatePromotionDates(UpdatePromotionBannerReq req, PromotionBannerEntity existing) {
        if (req.getStartDate() != null && !req.getStartDate().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Ngày bắt đầu promotion banner phải lớn hơn thời gian hiện tại");
        }

        if (existing.getStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu promotion banner là bắt buộc");
        }

        if (existing.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày kết thúc promotion banner là bắt buộc");
        }

        if (!existing.getEndDate().isAfter(existing.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc promotion banner phải lớn hơn ngày bắt đầu");
        }
    }
}
