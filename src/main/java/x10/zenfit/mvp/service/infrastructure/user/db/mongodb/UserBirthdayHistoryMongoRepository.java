package x10.zenfit.mvp.service.infrastructure.user.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserBirthdayHistoryMongoRepository extends MongoRepository<UserBirthdayHistoryDocument, String> {
}
