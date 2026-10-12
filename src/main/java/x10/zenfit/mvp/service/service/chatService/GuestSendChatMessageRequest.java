package x10.zenfit.mvp.service.service.chatService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GuestSendChatMessageRequest extends SendChatMessageRequest {

    @NotBlank(message = "Guest id khong duoc de trong")
    @Size(max = 120, message = "Guest id toi da 120 ky tu")
    private String guestId;
}

