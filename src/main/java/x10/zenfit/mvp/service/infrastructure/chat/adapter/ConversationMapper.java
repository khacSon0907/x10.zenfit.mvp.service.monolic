package x10.zenfit.mvp.service.infrastructure.chat.adapter;

import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.common.domain.entities.chat.ConversationEntity;
import x10.zenfit.mvp.service.infrastructure.chat.db.mongodb.ConversationDocument;

@Component
public class ConversationMapper {

    public ConversationDocument toDocument(ConversationEntity entity) {
        if (entity == null) {
            return null;
        }

        return ConversationDocument.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .adminId(entity.getAdminId())
                .lastMessage(entity.getLastMessage())
                .lastSender(entity.getLastSender())
                .lastMessageTime(entity.getLastMessageTime())
                .unreadCount(entity.getUnreadCount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ConversationEntity toEntity(ConversationDocument document) {
        if (document == null) {
            return null;
        }

        return ConversationEntity.builder()
                .id(document.getId())
                .customerId(document.getCustomerId())
                .adminId(document.getAdminId())
                .lastMessage(document.getLastMessage())
                .lastSender(document.getLastSender())
                .lastMessageTime(document.getLastMessageTime())
                .unreadCount(document.getUnreadCount())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}