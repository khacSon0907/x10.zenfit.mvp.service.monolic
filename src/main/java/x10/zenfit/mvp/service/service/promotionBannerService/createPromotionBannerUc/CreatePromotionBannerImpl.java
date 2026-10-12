package x10.zenfit.mvp.service.service.promotionBannerService.createPromotionBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPromotionBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerMapper;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerResp;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatePromotionBannerImpl implements ICreatePromotionBannerUc {

    private final IPromotionBannerRepository promotionBannerRepository;

    @Override
    public PromotionBannerResp createPromotionBanner(CreatePromotionBannerReq req) {
        validatePromotionDates(req);

        PromotionBannerEntity entity = PromotionBannerEntity.builder()
                .id(UUID.randomUUID().toString())
                .title(req.getTitle().trim())
                .active(req.getActive() == null || req.getActive())
                .sortOrder(req.getSortOrder() == null ? 0 : req.getSortOrder())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .build();

        PromotionBannerEntity saved = promotionBannerRepository.save(entity);
        return PromotionBannerMapper.toResp(saved);
    }

    private void validatePromotionDates(CreatePromotionBannerReq req) {
        Instant now = Instant.now();

        if (req.getStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu promotion banner là bắt buộc");
        }

        if (req.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày kết thúc promotion banner là bắt buộc");
        }

        if (!req.getStartDate().isAfter(now)) {
            throw new IllegalArgumentException("Ngày bắt đầu promotion banner phải lớn hơn thời gian hiện tại");
        }

        if (!req.getEndDate().isAfter(req.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc promotion banner phải lớn hơn ngày bắt đầu");
        }
    }
}
