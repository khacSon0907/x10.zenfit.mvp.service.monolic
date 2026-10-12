package x10.zenfit.mvp.service.infrastructure.chat.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMongoRepository
        extends MongoRepository<MessageDocument, String> {

    List<MessageDocument> findByConversationIdOrderByCreatedAtAsc(
            String conversationId
    );

    void deleteByConversationId(String conversationId);

}
