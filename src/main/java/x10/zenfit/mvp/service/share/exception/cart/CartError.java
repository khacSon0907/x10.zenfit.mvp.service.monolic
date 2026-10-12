package x10.zenfit.mvp.service.share.exception.cart;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum CartError implements ErrorDescriptor {

    CATEGORY_NOT_FOUND(
            "BUSINESS",
            404,
            "CATEGORY.NOT_FOUND",
            "Không tìm thấy danh mục"
    ),

    CART_NOT_FOUND(
            "BUSINESS",
            404,
            "CART.NOT_FOUND",
            "Không tìm thấy giỏ hàng"
    ),

    CART_ITEM_NOT_FOUND(
            "BUSINESS",
            404,
            "CART.ITEM_NOT_FOUND",
            "Không tìm thấy sản phẩm trong giỏ hàng"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    CartError(String type, int httpStatus, String code, String defaultMessage) {
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
