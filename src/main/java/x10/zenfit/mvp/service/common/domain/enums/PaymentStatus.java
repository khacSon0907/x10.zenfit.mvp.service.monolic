package x10.zenfit.mvp.service.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatus {
    PENDING,
    UNPAID,
    PAID,
    SUCCESS,
    REFUNDED,
    FAILED;

    @JsonCreator
    public static PaymentStatus from(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }

        String normalized = value.trim().toUpperCase();
        if ("UNPAID".equals(normalized)) {
            return PENDING;
        }
        if ("SUCCESS".equals(normalized)) {
            return PAID;
        }

        return PaymentStatus.valueOf(normalized);
    }
}
