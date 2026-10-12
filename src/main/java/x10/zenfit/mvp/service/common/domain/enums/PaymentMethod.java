package x10.zenfit.mvp.service.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentMethod {
    COD,
    PAYOS;

    @JsonCreator
    public static PaymentMethod from(String value) {
        if (value == null || value.isBlank()) {
            return COD;
        }

        return PaymentMethod.valueOf(value.trim().toUpperCase());
    }
}
