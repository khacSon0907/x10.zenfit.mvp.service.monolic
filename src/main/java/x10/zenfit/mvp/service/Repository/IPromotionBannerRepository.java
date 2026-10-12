package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;

import java.util.List;
import java.util.Optional;

public interface IPromotionBannerRepository {
    PromotionBannerEntity save(PromotionBannerEntity promotionBanner);
    Optional<PromotionBannerEntity> findById(String id);
    List<PromotionBannerEntity> findAll();
    List<PromotionBannerEntity> findAllActive();
    void deleteById(String id);
}
