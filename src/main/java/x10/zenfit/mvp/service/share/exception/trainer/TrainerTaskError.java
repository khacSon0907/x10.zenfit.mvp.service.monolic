package x10.zenfit.mvp.service.share.exception.trainer;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum TrainerTaskError implements ErrorDescriptor {

    TASK_NOT_FOUND(
            "BUSINESS",
            404,
            "TRAINER_TASK.NOT_FOUND",
            "Không tìm thấy thông tin công việc / hoạt động"
    ),

    TASK_INVALID_DATA(
            "BUSINESS",
            400,
            "TRAINER_TASK.INVALID_DATA",
            "Dữ liệu công việc / hoạt động không hợp lệ"
    ),

    TASK_ACCESS_DENIED(
            "BUSINESS",
            403,
            "TRAINER_TASK.ACCESS_DENIED",
            "Bạn không có quyền thao tác trên công việc / hoạt động này"
    ),

    TASK_TIME_INVALID(
            "BUSINESS",
            400,
            "TRAINER_TASK.TIME_INVALID",
            "Thời gian kết thúc phải sau thời gian bắt đầu"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    TrainerTaskError(String type, int httpStatus, String code, String defaultMessage) {
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

