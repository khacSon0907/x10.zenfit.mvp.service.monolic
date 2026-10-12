package x10.zenfit.mvp.service.infrastructure.chat.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConversationMongoRepository
        extends MongoRepository<ConversationDocument, String> {

    Optional<ConversationDocument> findByCustomerId(String customerId);

    List<ConversationDocument> findByAdminId(String adminId);

    List<ConversationDocument> findAllByOrderByUpdatedAtDesc();

}
