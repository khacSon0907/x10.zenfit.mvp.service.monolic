package x10.zenfit.mvp.service.infrastructure.promotionBanner.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IPromotionBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.PromotionBannerEntity;
import x10.zenfit.mvp.service.infrastructure.promotionBanner.db.mongodb.PromotionBannerMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PromotionBannerRepositoryImpl implements IPromotionBannerRepository {

    private final PromotionBannerMongoRepository promotionBannerMongoRepository;

    @Override
    public PromotionBannerEntity save(PromotionBannerEntity promotionBanner) {
        var document = PromotionBannerMapper.toDocument(promotionBanner);
        var saved = promotionBannerMongoRepository.save(document);
        return PromotionBannerMapper.toEntity(saved);
    }

    @Override
    public Optional<PromotionBannerEntity> findById(String id) {
        return promotionBannerMongoRepository.findById(id)
                .map(PromotionBannerMapper::toEntity);
    }

    @Override
    public List<PromotionBannerEntity> findAll() {
        return promotionBannerMongoRepository.findAll()
                .stream()
                .map(PromotionBannerMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionBannerEntity> findAllActive() {
        return promotionBannerMongoRepository.findByActiveTrue()
                .stream()
                .map(PromotionBannerMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        promotionBannerMongoRepository.deleteById(id);
    }
}
