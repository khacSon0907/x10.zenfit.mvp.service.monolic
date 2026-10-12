package x10.zenfit.mvp.service.service.chatService;

import lombok.Builder;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.ConversationStatus;
import x10.zenfit.mvp.service.common.domain.enums.SenderType;

import java.time.Instant;

@Data
@Builder
public class ChatConversationResponse {

    private String id;
    private String customerId;
    private String customerName;
    private String customerAvatarUrl;
    private String adminId;
    private String adminName;
    private String adminAvatarUrl;
    private String lastMessage;
    private SenderType lastSender;
    private Instant lastMessageTime;
    private Integer unreadCount;
    private ConversationStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
