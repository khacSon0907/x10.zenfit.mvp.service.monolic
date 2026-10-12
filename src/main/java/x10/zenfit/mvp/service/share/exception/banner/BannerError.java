package x10.zenfit.mvp.service.share.exception.banner;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum BannerError implements ErrorDescriptor {
    BANNER_NOT_FOUND(
            "BUSINESS",
            404,
            "BANNER.NOT_FOUND",
            "Không tìm thấy banner"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    BannerError(String type, int httpStatus, String code, String defaultMessage) {
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
