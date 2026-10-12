package x10.zenfit.mvp.service.share.exception.order;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum ShippingRuleError implements ErrorDescriptor {
    SHIPPING_RULE_NOT_FOUND(
            "BUSINESS",
            404,
            "SHIPPING_RULE.NOT_FOUND",
            "Khong tim thay cau hinh phi ship"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    ShippingRuleError(String type, int httpStatus, String code, String defaultMessage) {
        this.type = type;
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public int httpStatus() { return httpStatus; }

    @Override
    public String code() { return code; }

    @Override
    public String defaultMessage() { return defaultMessage; }

    @Override
    public String type() { return type; }
}
