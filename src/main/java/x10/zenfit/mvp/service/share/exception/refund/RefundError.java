package x10.zenfit.mvp.service.share.exception.refund;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum RefundError implements ErrorDescriptor {

    REFUND_NOT_FOUND("BUSINESS", 404, "REFUND.NOT_FOUND", "Khong tim thay yeu cau hoan tien"),
    INVALID_REFUND_DATA("BUSINESS", 400, "REFUND.INVALID_DATA", "Du lieu hoan tien khong hop le"),
    REFUND_NOT_ALLOWED("BUSINESS", 400, "REFUND.NOT_ALLOWED", "Don hang khong du dieu kien hoan tien"),
    REFUND_ALREADY_EXISTS("BUSINESS", 409, "REFUND.ALREADY_EXISTS", "Don hang da co yeu cau hoan tien dang xu ly"),
    REFUND_PROVIDER_FAILED("BUSINESS", 502, "REFUND.PROVIDER_FAILED", "Cong thanh toan xu ly hoan tien that bai");

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    RefundError(String type, int httpStatus, String code, String defaultMessage) {
        this.type = type;
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public int httpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }
}
