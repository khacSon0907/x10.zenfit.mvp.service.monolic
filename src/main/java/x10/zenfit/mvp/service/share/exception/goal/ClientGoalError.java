package x10.zenfit.mvp.service.share.exception.goal;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum ClientGoalError implements ErrorDescriptor {

    CLIENT_GOAL_NOT_FOUND(
            "BUSINESS",
            404,
            "CLIENT_GOAL.NOT_FOUND",
            "Không tìm thấy mục tiêu của khách hàng"
    ),

    INVALID_CLIENT_GOAL_DATA(
            "BUSINESS",
            400,
            "CLIENT_GOAL.INVALID_DATA",
            "Dữ liệu mục tiêu khách hàng không hợp lệ"
    ),

    CLIENT_GOAL_INVALID_DATE_RANGE(
            "BUSINESS",
            400,
            "CLIENT_GOAL.INVALID_DATE_RANGE",
            "Ngày kết thúc phải sau ngày bắt đầu"
    ),

    CLIENT_GOAL_INVALID_WEIGHT(
            "BUSINESS",
            400,
            "CLIENT_GOAL.INVALID_WEIGHT",
            "Cân nặng mục tiêu không hợp lệ"
    ),

    CLIENT_GOAL_ALREADY_COMPLETED(
            "BUSINESS",
            409,
            "CLIENT_GOAL.ALREADY_COMPLETED",
            "Mục tiêu khách hàng đã được hoàn thành"
    ),

    CLIENT_GOAL_ALREADY_CANCELLED(
            "BUSINESS",
            409,
            "CLIENT_GOAL.ALREADY_CANCELLED",
            "Mục tiêu khách hàng đã bị hủy"
    ),

    CLIENT_GOAL_CANNOT_BE_COMPLETED(
            "BUSINESS",
            409,
            "CLIENT_GOAL.CANNOT_BE_COMPLETED",
            "Không thể hoàn thành mục tiêu khách hàng ở trạng thái hiện tại"
    ),

    CLIENT_GOAL_CANNOT_BE_CANCELLED(
            "BUSINESS",
            409,
            "CLIENT_GOAL.CANNOT_BE_CANCELLED",
            "Không thể hủy mục tiêu khách hàng ở trạng thái hiện tại"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    ClientGoalError(String type, int httpStatus, String code, String defaultMessage) {
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