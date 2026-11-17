package x10.zenfit.mvp.service.share.exception.subscription;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum UserSubscriptionError implements ErrorDescriptor {

    SUBSCRIPTION_NOT_FOUND(
            "BUSINESS",
            404,
            "USER_SUBSCRIPTION.NOT_FOUND",
            "Không tìm thấy thông tin đăng ký gói của người dùng"
    ),

    NO_ACTIVE_SUBSCRIPTION(
            "BUSINESS",
            404,
            "USER_SUBSCRIPTION.NO_ACTIVE",
            "Bạn chưa chọn hoặc chưa có gói dịch vụ nào đang hoạt động"
    ),

    PLAN_NOT_FOUND(
            "BUSINESS",
            404,
            "USER_SUBSCRIPTION.PLAN_NOT_FOUND",
            "Gói dịch vụ được chọn không tồn tại"
    ),

    PLAN_NOT_ACTIVE(
            "BUSINESS",
            400,
            "USER_SUBSCRIPTION.PLAN_NOT_ACTIVE",
            "Gói dịch vụ này hiện không mở đăng ký"
    ),

    ALREADY_HAS_ACTIVE_SUBSCRIPTION(
            "BUSINESS",
            409,
            "USER_SUBSCRIPTION.ALREADY_ACTIVE",
            "Bạn đang có gói dịch vụ đang hoạt động"
    ),

    CANNOT_CANCEL_INACTIVE_SUBSCRIPTION(
            "BUSINESS",
            400,
            "USER_SUBSCRIPTION.CANNOT_CANCEL",
            "Chỉ có thể hủy gói dịch vụ đang hoạt động"
    ),

    ACCESS_DENIED(
            "BUSINESS",
            403,
            "USER_SUBSCRIPTION.ACCESS_DENIED",
            "Bạn không có quyền thao tác trên gói dịch vụ này"
    ),

    INVALID_SUBSCRIPTION_DATA(
            "BUSINESS",
            400,
            "USER_SUBSCRIPTION.INVALID_DATA",
            "Dữ liệu đăng ký gói dịch vụ không hợp lệ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    UserSubscriptionError(String type, int httpStatus, String code, String defaultMessage) {
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

