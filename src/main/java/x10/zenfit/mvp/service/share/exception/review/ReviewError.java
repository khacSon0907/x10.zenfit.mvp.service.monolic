package x10.zenfit.mvp.service.share.exception.review;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum ReviewError implements ErrorDescriptor {

    REVIEW_NOT_FOUND(
            "BUSINESS",
            404,
            "REVIEW.NOT_FOUND",
            "Khong tim thay danh gia"
    ),

    INVALID_RATING(
            "BUSINESS",
            400,
            "REVIEW.INVALID_RATING",
            "So sao danh gia phai nam trong khoang 0 den 5"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    ReviewError(String type, int httpStatus, String code, String defaultMessage) {
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
