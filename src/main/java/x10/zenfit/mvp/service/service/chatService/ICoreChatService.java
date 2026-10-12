package x10.zenfit.mvp.service.service.chatService;

import java.util.List;
import x10.zenfit.mvp.service.common.domain.enums.SenderType;

public interface ICoreChatService {

    ChatConversationResponse getOrCreateMyConversation(String customerId);

    ChatConversationResponse getOrCreateGuestConversation(String guestId);

    List<ChatConversationResponse> getSupportConversations();

    ChatConversationResponse acceptConversation(String conversationId, String supportUserId);

    ChatConversationResponse closeConversation(String conversationId, String supportUserId);

    void deleteConversation(String conversationId);

    List<ChatMessageResponse> getMessages(String conversationId, String currentUserId, boolean supportUser);

    List<ChatMessageResponse> getGuestMessages(String conversationId, String guestId);

    ChatMessageResponse sendCustomerMessage(String conversationId, String customerId, SendChatMessageRequest request);

    ChatMessageResponse sendGuestMessage(String conversationId, String guestId, SendChatMessageRequest request);

    ChatMessageResponse sendSupportMessage(
            String conversationId,
            String supportUserId,
            SenderType senderType,
            SendChatMessageRequest request
    );
}
