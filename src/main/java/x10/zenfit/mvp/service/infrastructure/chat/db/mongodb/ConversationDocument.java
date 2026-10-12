package x10.zenfit.mvp.service.infrastructure.chat.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.ConversationStatus;
import x10.zenfit.mvp.service.common.domain.enums.SenderType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversations")
public class ConversationDocument {
    @Id
    private String id;
    /**
     * ID khách hàng
     */
    private String customerId;

    /**
     * ID admin đang hỗ trợ
     * (có thể null nếu AI đang xử lý)
     */
    private String adminId;

    /**
     * Tin nhắn cuối cùng
     */
    private String lastMessage;

    /**
     * Người gửi tin nhắn cuối
     */
    private SenderType lastSender;

    /**
     * Thời gian tin nhắn cuối
     */
    private Instant lastMessageTime;

    /**
     * Số tin nhắn chưa đọc
     */
    @Builder.Default
    private Integer unreadCount = 0;

    /**
     * Trạng thái cuộc hội thoại
     */
    private ConversationStatus status;

    /**
     * Thời gian tạo
     */
    private Instant createdAt;

    /**
     * Thời gian cập nhật
     */
    private Instant updatedAt;
}
