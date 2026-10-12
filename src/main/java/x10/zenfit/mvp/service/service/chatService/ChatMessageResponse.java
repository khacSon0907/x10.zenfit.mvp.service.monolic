package x10.zenfit.mvp.service.service.chatService;

import lombok.Builder;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.SenderType;

import java.time.Instant;

@Data
@Builder
public class ChatMessageResponse {

    private String id;
    private String conversationId;
    private String senderId;
    private String senderName;
    private String senderAvatarUrl;
    private SenderType senderType;
    private String content;
    private String imageUrl;
    private String imagePublicId;
    private Boolean seen;
    private Instant createdAt;
}
