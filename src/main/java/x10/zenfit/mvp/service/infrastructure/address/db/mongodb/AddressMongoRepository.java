package x10.zenfit.mvp.service.infrastructure.address.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AddressMongoRepository extends MongoRepository<AddressDocument, String> {
    List<AddressDocument> findByUserId(String userId);
}
