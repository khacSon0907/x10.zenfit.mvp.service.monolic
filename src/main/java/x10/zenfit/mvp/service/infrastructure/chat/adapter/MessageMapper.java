package x10.zenfit.mvp.service.infrastructure.chat.adapter;

import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.common.domain.entities.chat.MessageEntity;
import x10.zenfit.mvp.service.infrastructure.chat.db.mongodb.MessageDocument;

@Component
public class MessageMapper {

    public MessageDocument toDocument(MessageEntity entity) {
        if (entity == null) {
            return null;
        }

        return MessageDocument.builder()
                .id(entity.getId())
                .conversationId(entity.getConversationId())
                .senderId(entity.getSenderId())
                .senderType(entity.getSenderType())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .imagePublicId(entity.getImagePublicId())
                .seen(entity.getSeen())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public MessageEntity toEntity(MessageDocument document) {
        if (document == null) {
            return null;
        }

        return MessageEntity.builder()
                .id(document.getId())
                .conversationId(document.getConversationId())
                .senderId(document.getSenderId())
                .senderType(document.getSenderType())
                .content(document.getContent())
                .imageUrl(document.getImageUrl())
                .imagePublicId(document.getImagePublicId())
                .seen(document.getSeen())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
