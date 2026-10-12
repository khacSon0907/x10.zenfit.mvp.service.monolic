package x10.zenfit.mvp.service.infrastructure.banner.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BannerMongoRepository extends MongoRepository<BannerDocument, String> {
    List<BannerDocument> findByActiveTrue();
}
