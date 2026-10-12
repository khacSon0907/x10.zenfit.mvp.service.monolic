package x10.zenfit.mvp.service.service.chatService;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendChatMessageRequest {

    @Size(max = 2000, message = "Noi dung tin nhan toi da 2000 ky tu")
    private String content;

    @Size(max = 1000, message = "URL anh toi da 1000 ky tu")
    private String imageUrl;

    @Size(max = 255, message = "Public id anh toi da 255 ky tu")
    private String imagePublicId;
}
