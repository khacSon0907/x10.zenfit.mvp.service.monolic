package x10.zenfit.mvp.service.share.exception.client;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum ClientError implements ErrorDescriptor {

    CLIENT_NOT_FOUND(
            "BUSINESS",
            404,
            "CLIENT.NOT_FOUND",
            "Không tìm thấy thông tin khách hàng"
    ),

    CLIENT_EMAIL_ALREADY_EXISTS(
            "BUSINESS",
            409,
            "CLIENT.EMAIL_ALREADY_EXISTS",
            "Email khách hàng đã tồn tại trên hệ thống"
    ),

    CLIENT_PHONE_ALREADY_EXISTS(
            "BUSINESS",
            409,
            "CLIENT.PHONE_ALREADY_EXISTS",
            "Số điện thoại khách hàng đã tồn tại trên hệ thống"
    ),

    INVALID_CLIENT_DATA(
            "BUSINESS",
            400,
            "CLIENT.INVALID_DATA",
            "Dữ liệu khách hàng không hợp lệ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    ClientError(String type, int httpStatus, String code, String defaultMessage) {
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
