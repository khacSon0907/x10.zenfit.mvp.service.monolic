package x10.zenfit.mvp.service.common.domain.enums;

public enum ConversationStatus {
    /**
     * AI đang trả lời
     */
    AI_HANDLING,

    /**
     * Chờ Admin hỗ trợ
     */
    WAITING_ADMIN,

    /**
     * Admin đang hỗ trợ
     */
    ADMIN_HANDLING,

    /**
     * Đã kết thúc
     */
    CLOSED
}
