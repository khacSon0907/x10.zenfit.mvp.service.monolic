package x10.zenfit.mvp.service.infrastructure.review.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewMongoRepository extends MongoRepository<ReviewDocument, String> {

    List<ReviewDocument> findByProductIdOrderByCreatedAtDesc(String productId);
}
