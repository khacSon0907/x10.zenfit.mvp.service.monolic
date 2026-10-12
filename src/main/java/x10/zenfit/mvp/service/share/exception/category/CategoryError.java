package x10.zenfit.mvp.service.share.exception.category;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum CategoryError implements ErrorDescriptor {
    CATEGORY_EXISTS(
            "BUSINESS",
            409,
            "CATEGORY.EXISTS",
            "Danh muc da ton tai"
    ),
    CATEGORY_NOT_FOUND(
            "BUSINESS",
            404,
            "CATEGORY.NOT_FOUND",
            "Khong tim thay danh muc"
    ),
    CATEGORY_HAS_CHILDREN(
            "BUSINESS",
            409,
            "CATEGORY.HAS_CHILDREN",
            "Khong the xoa danh muc khi con danh muc con"
    ),
    CATEGORY_INVALID_PARENT(
            "BUSINESS",
            400,
            "CATEGORY.INVALID_PARENT",
            "Parent category khong hop le"
    ),
    CATEGORY_CYCLE_DETECTED(
            "BUSINESS",
            400,
            "CATEGORY.CYCLE_DETECTED",
            "Khong the tao vong lap category"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    CategoryError(String type, int httpStatus, String code, String defaultMessage) {
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
