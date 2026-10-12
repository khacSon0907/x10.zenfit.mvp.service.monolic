package x10.zenfit.mvp.service.service.chatService;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GuestChatConversationRequest {

    @Size(max = 120, message = "Guest id toi da 120 ky tu")
    private String guestId;
}

