package x10.zenfit.mvp.service.share.exception.address;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum AddressError implements ErrorDescriptor {

    ADDRESS_NOT_FOUND(
            "BUSINESS",
            404,
            "ADDRESS.NOT_FOUND",
            "Không tìm thấy địa chỉ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    AddressError(String type, int httpStatus, String code, String defaultMessage) {
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
