package x10.zenfit.mvp.service.share.exception.product;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum ProductError implements ErrorDescriptor {

    PRODUCT_NOT_FOUND(
            "BUSINESS",
            404,
            "PRODUCT.NOT_FOUND",
            "Không tìm thấy sản phẩm"
    ),

    PRODUCT_ALREADY_EXISTS(
            "BUSINESS",
            409,
            "PRODUCT.ALREADY_EXISTS",
            "Sản phẩm đã tồn tại"
    ),

    INVALID_PRODUCT_DATA(
            "BUSINESS",
            400,
            "PRODUCT.INVALID_DATA",
            "Dữ liệu sản phẩm không hợp lệ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    ProductError(String type, int httpStatus, String code, String defaultMessage) {
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
