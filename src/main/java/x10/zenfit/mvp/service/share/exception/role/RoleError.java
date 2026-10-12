package x10.zenfit.mvp.service.share.exception.role;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum RoleError implements ErrorDescriptor {
    ROLE_EXISTS(
            "BUSINESS",
            409,
            "ROLE.EXISTS",
            "Role da ton tai"
    ),
    ROLE_NOT_FOUND(
            "BUSINESS",
            404,
            "ROLE.NOT_FOUND",
            "Khong tim thay role"
    ),
    ROLE_INVALID_CODE(
            "VALIDATION",
            400,
            "ROLE.INVALID_CODE",
            "Role code khong hop le"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    RoleError(String type, int httpStatus, String code, String defaultMessage) {
        this.type = type;
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
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

    @Override
    public String type() {
        return type;
    }
}
