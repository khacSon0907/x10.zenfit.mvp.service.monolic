package x10.zenfit.mvp.service.share.exception.order;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum OrderError implements ErrorDescriptor {

    ORDER_NOT_FOUND(
            "BUSINESS",
            404,
            "ORDER.NOT_FOUND",
            "Khong tim thay don hang"
    ),

    INVALID_ORDER_DATA(
            "BUSINESS",
            400,
            "ORDER.INVALID_DATA",
            "Du lieu don hang khong hop le"
    ),

    ORDER_ALREADY_EXISTS(
            "BUSINESS",
            409,
            "ORDER.ALREADY_EXISTS",
            "Don hang da ton tai"
    ),

    INVALID_ORDER_STATUS(
            "BUSINESS",
            400,
            "ORDER.INVALID_STATUS",
            "Trang thai don hang khong hop le"
    ),

    INSUFFICIENT_STOCK(
            "BUSINESS",
            409,
            "ORDER.INSUFFICIENT_STOCK",
            "So luong ton kho khong du"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    OrderError(String type, int httpStatus, String code, String defaultMessage) {
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
