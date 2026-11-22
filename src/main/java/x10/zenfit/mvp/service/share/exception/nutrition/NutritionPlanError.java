package x10.zenfit.mvp.service.share.exception.nutrition;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum NutritionPlanError implements ErrorDescriptor {

    PLAN_NOT_FOUND(
            "BUSINESS",
            404,
            "NUTRITION_PLAN.NOT_FOUND",
            "Không tìm thấy kế hoạch dinh dưỡng"
    ),

    FOOD_NOT_FOUND(
            "BUSINESS",
            404,
            "NUTRITION_PLAN.FOOD_NOT_FOUND",
            "Không tìm thấy món ăn trong kế hoạch"
    ),

    CLIENT_NOT_FOUND(
            "BUSINESS",
            404,
            "NUTRITION_PLAN.CLIENT_NOT_FOUND",
            "Không tìm thấy thông tin khách hàng"
    ),

    ACCESS_DENIED(
            "BUSINESS",
            403,
            "NUTRITION_PLAN.ACCESS_DENIED",
            "Bạn không có quyền thao tác trên kế hoạch dinh dưỡng này"
    ),

    CLIENT_NOT_ASSIGNED_TO_TRAINER(
            "BUSINESS",
            403,
            "NUTRITION_PLAN.CLIENT_NOT_ASSIGNED",
            "Khách hàng này không thuộc quyền quản lý của bạn"
    ),

    INVALID_DATA(
            "BUSINESS",
            400,
            "NUTRITION_PLAN.INVALID_DATA",
            "Dữ liệu kế hoạch dinh dưỡng không hợp lệ"
    ),

    INVALID_DATE_RANGE(
            "BUSINESS",
            400,
            "NUTRITION_PLAN.INVALID_DATE_RANGE",
            "Ngày bắt đầu không được lớn hơn ngày kết thúc"
    ),

    INVALID_MACRO_TARGET(
            "BUSINESS",
            400,
            "NUTRITION_PLAN.INVALID_MACRO_TARGET",
            "Thông số Macro mục tiêu không hợp lệ"
    ),

    INVALID_FOOD_DATA(
            "BUSINESS",
            400,
            "NUTRITION_PLAN.INVALID_FOOD_DATA",
            "Thông tin món ăn không hợp lệ"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    NutritionPlanError(String type, int httpStatus, String code, String defaultMessage) {
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

