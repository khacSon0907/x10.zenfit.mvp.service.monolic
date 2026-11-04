package x10.zenfit.mvp.service.share.exception.session;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum TrainingSessionError implements ErrorDescriptor {

    TRAINING_SESSION_NOT_FOUND(
            "BUSINESS",
            404,
            "SESSION.NOT_FOUND",
            "Không tìm thấy thông tin buổi tập"
    ),

    TRAINING_SESSION_CONFLICT(
            "BUSINESS",
            409,
            "SESSION.CONFLICT",
            "Thời điểm tập bị trùng với lịch tập khác của huấn luyện viên hoặc học viên"
    ),

    TRAINING_SESSION_INVALID_STATUS(
            "BUSINESS",
            400,
            "SESSION.INVALID_STATUS",
            "Trạng thái hiện tại của buổi tập không hợp lệ để thực hiện thao tác này"
    ),

    TRAINING_SESSION_ACCESS_DENIED(
            "BUSINESS",
            403,
            "SESSION.ACCESS_DENIED",
            "Bạn không có quyền xem hoặc thao tác trên buổi tập này"
    ),

    TRAINING_SESSION_INVALID_DATA(
            "BUSINESS",
            400,
            "SESSION.INVALID_DATA",
            "Dữ liệu buổi tập không hợp lệ"
    ),

    TRAINING_SESSION_INVALID_DATE_RANGE(
            "BUSINESS",
            400,
            "SESSION.INVALID_DATE_RANGE",
            "Khoảng thời gian truy vấn không hợp lệ"
    ),

    CONTRACT_NOT_ACTIVE(
            "BUSINESS",
            400,
            "SESSION.CONTRACT_NOT_ACTIVE",
            "Hợp đồng PT liên kết không ở trạng thái hoạt động (ACTIVE)"
    ),

    CONTRACT_NOT_BELONG_TO_CLIENT(
            "BUSINESS",
            400,
            "SESSION.CONTRACT_NOT_BELONG_TO_CLIENT",
            "Hợp đồng PT không thuộc về học viên được chỉ định"
    ),

    TRAINING_SESSION_IN_PAST(
            "BUSINESS",
            400,
            "SESSION.IN_PAST",
            "Thời gian bắt đầu buổi tập phải lớn hơn thời gian hiện tại"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    TrainingSessionError(String type, int httpStatus, String code, String defaultMessage) {
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

