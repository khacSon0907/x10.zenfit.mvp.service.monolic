package x10.zenfit.mvp.service.service.promotionBannerService.getPromotionBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPromotionBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerMapper;
import x10.zenfit.mvp.service.service.promotionBannerService.PromotionBannerResp;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPromotionBannersImpl implements IGetPromotionBannersUc {

    private final IPromotionBannerRepository promotionBannerRepository;

    @Override
    public List<PromotionBannerResp> getAll() {
        return promotionBannerRepository.findAll()
                .stream()
                .map(PromotionBannerMapper::toResp)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionBannerResp> getAllActive() {
        return promotionBannerRepository.findAllActive()
                .stream()
                .map(PromotionBannerMapper::toResp)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionBannerResp> getCurrentlyVisible() {
        Instant now = Instant.now();

        return promotionBannerRepository.findAllActive()
                .stream()
                .filter(promotionBanner -> isVisibleNow(promotionBanner, now))
                .sorted(Comparator.comparing(
                        promotionBanner -> promotionBanner.getSortOrder() == null
                                ? Integer.MAX_VALUE
                                : promotionBanner.getSortOrder()
                ))
                .map(PromotionBannerMapper::toResp)
                .collect(Collectors.toList());
    }

    private boolean isVisibleNow(PromotionBannerEntity promotionBanner, Instant now) {
        boolean hasStarted = promotionBanner.getStartDate() == null
                || !promotionBanner.getStartDate().isAfter(now);
        boolean hasNotExpired = promotionBanner.getEndDate() == null
                || promotionBanner.getEndDate().isAfter(now);

        return promotionBanner.isActive() && hasStarted && hasNotExpired;
    }
}
