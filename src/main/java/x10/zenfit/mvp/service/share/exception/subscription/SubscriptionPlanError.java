package x10.zenfit.mvp.service.share.exception.subscription;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum SubscriptionPlanError implements ErrorDescriptor {

    PLAN_NOT_FOUND(
            "BUSINESS",
            404,
            "SUBSCRIPTION_PLAN.NOT_FOUND",
            "Không tìm thấy thông tin gói dịch vụ"
    ),

    CODE_ALREADY_EXISTS(
            "BUSINESS",
            409,
            "SUBSCRIPTION_PLAN.CODE_ALREADY_EXISTS",
            "Mã gói dịch vụ đã tồn tại trong hệ thống"
    ),

    CANNOT_DELETE_DEFAULT_PLAN(
            "BUSINESS",
            400,
            "SUBSCRIPTION_PLAN.CANNOT_DELETE_DEFAULT",
            "Không thể xóa hoặc lưu trữ gói dịch vụ mặc định"
    ),

    CANNOT_MODIFY_ARCHIVED_PLAN(
            "BUSINESS",
            400,
            "SUBSCRIPTION_PLAN.CANNOT_MODIFY_ARCHIVED",
            "Không thể chỉnh sửa hoặc kích hoạt lại gói dịch vụ đã lưu trữ"
    ),

    INVALID_PLAN_DATA(
            "BUSINESS",
            400,
            "SUBSCRIPTION_PLAN.INVALID_DATA",
            "Dữ liệu cấu hình gói dịch vụ không hợp lệ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    SubscriptionPlanError(String type, int httpStatus, String code, String defaultMessage) {
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

