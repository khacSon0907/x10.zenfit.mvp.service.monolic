package x10.zenfit.mvp.service.infrastructure.cart.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartMongoRepository extends MongoRepository<CartDocument, String> {
    Optional<CartDocument> findByUserId(String userId);
    void deleteByUserId(String userId);
}

