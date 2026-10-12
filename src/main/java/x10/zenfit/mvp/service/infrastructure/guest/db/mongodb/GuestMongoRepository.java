package x10.zenfit.mvp.service.infrastructure.guest.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GuestMongoRepository extends MongoRepository<GuestDocument, String> {

    Optional<GuestDocument> findByEmail(String email);
}
