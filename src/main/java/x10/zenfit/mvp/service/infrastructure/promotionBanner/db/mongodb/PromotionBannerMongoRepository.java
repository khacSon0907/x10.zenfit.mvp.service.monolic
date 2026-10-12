package x10.zenfit.mvp.service.infrastructure.promotionBanner.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PromotionBannerMongoRepository extends MongoRepository<PromotionBannerDocument, String> {
    List<PromotionBannerDocument> findByActiveTrue();
}
