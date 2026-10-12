package x10.zenfit.mvp.service.infrastructure.favorite.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FavoriteMongoRepository extends MongoRepository<FavoriteDocument, String> {
    Optional<FavoriteDocument> findByUserId(String userId);
    void deleteByUserId(String userId);
}

