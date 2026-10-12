package x10.zenfit.mvp.service.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PENDING,

    CONFIRMED,

    PACKING,

    SHIPPING,

    COMPLETED,

    RETURNED,

    EXCHANGED,

    REFUNDED,

    CANCELLED,

    // Legacy statuses are kept so old MongoDB/API values can still be read.
    DELIVERED,

    RETURN_REQUESTED,

    RETURN_APPROVED,

    RETURN_RECEIVED,

    EXCHANGE_APPROVED,

    REFUND_PROCESSING,

    REFUND_SUCCESS;

    @JsonCreator
    public static OrderStatus from(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }

        String normalized = value.trim().toUpperCase()
                .replace("-", "_")
                .replace(" ", "_");

        return switch (normalized) {
            case "CHUA_XAC_NHAN", "CHO_XAC_NHAN", "WAITING_CONFIRM", "UNCONFIRMED" -> PENDING;
            case "XAC_NHAN", "DA_XAC_NHAN", "APPROVED" -> CONFIRMED;
            case "DONG_GOI", "DANG_DONG_GOI", "CHO_LAY_HANG", "PACKED" -> PACKING;
            case "GIAO_HANG", "DANG_GIAO", "DELIVERING" -> SHIPPING;
            case "THANH_CONG", "DA_GIAO", "DELIVERED", "DONE", "SUCCESS" -> COMPLETED;
            case "TRA_HANG", "RETURN", "RETURN_APPROVED", "RETURN_RECEIVED", "RETURN_REQUESTED" -> RETURNED;
            case "DOI_HANG", "EXCHANGE", "EXCHANGE_APPROVED" -> EXCHANGED;
            case "HOAN_TIEN_THANH_CONG", "REFUND_SUCCESS", "REFUNDED_SUCCESS" -> REFUNDED;
            case "HUY", "DA_HUY", "CANCELED" -> CANCELLED;
            default -> OrderStatus.valueOf(normalized);
        };
    }
}
