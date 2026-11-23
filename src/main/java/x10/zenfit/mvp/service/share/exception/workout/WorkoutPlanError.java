package x10.zenfit.mvp.service.share.exception.workout;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum WorkoutPlanError implements ErrorDescriptor {

    PLAN_NOT_FOUND(
            "BUSINESS",
            404,
            "WORKOUT_PLAN.NOT_FOUND",
            "Không tìm thấy giáo án tập luyện"
    ),

    DAY_NOT_FOUND(
            "BUSINESS",
            404,
            "WORKOUT_PLAN.DAY_NOT_FOUND",
            "Không tìm thấy ngày tập trong giáo án"
    ),

    EXERCISE_NOT_FOUND(
            "BUSINESS",
            404,
            "WORKOUT_PLAN.EXERCISE_NOT_FOUND",
            "Không tìm thấy bài tập trong ngày tập"
    ),

    CLIENT_NOT_FOUND(
            "BUSINESS",
            404,
            "WORKOUT_PLAN.CLIENT_NOT_FOUND",
            "Không tìm thấy thông tin khách hàng"
    ),

    ACCESS_DENIED(
            "BUSINESS",
            403,
            "WORKOUT_PLAN.ACCESS_DENIED",
            "Bạn không có quyền thao tác trên giáo án tập luyện này"
    ),

    CLIENT_NOT_ASSIGNED_TO_TRAINER(
            "BUSINESS",
            403,
            "WORKOUT_PLAN.CLIENT_NOT_ASSIGNED",
            "Khách hàng này không thuộc quyền quản lý của bạn"
    ),

    INVALID_DATA(
            "BUSINESS",
            400,
            "WORKOUT_PLAN.INVALID_DATA",
            "Dữ liệu giáo án tập luyện không hợp lệ"
    ),

    INVALID_DATE_RANGE(
            "BUSINESS",
            400,
            "WORKOUT_PLAN.INVALID_DATE_RANGE",
            "Ngày bắt đầu không được lớn hơn ngày kết thúc"
    ),

    INVALID_DAY_DATA(
            "BUSINESS",
            400,
            "WORKOUT_PLAN.INVALID_DAY_DATA",
            "Thông tin ngày tập không hợp lệ"
    ),

    INVALID_EXERCISE_DATA(
            "BUSINESS",
            400,
            "WORKOUT_PLAN.INVALID_EXERCISE_DATA",
            "Thông tin bài tập không hợp lệ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    WorkoutPlanError(String type, int httpStatus, String code, String defaultMessage) {
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

