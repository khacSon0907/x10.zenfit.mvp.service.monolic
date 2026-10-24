package x10.zenfit.mvp.service.share.exception.contract;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum PtContractError implements ErrorDescriptor {

    CONTRACT_NOT_FOUND(
            "BUSINESS",
            404,
            "CONTRACT.NOT_FOUND",
            "Không tìm thấy thông tin hợp đồng PT"
    ),

    INVALID_CONTRACT_DATA(
            "BUSINESS",
            400,
            "CONTRACT.INVALID_DATA",
            "Dữ liệu hợp đồng không hợp lệ"
    ),

    CLIENT_INFO_REQUIRED(
            "BUSINESS",
            400,
            "CONTRACT.CLIENT_INFO_REQUIRED",
            "Vui lòng cung cấp mã khách hàng hoặc thông tin khách hàng để tạo hợp đồng"
    ),

    CONTRACT_STATUS_INVALID(
            "BUSINESS",
            400,
            "CONTRACT.STATUS_INVALID",
            "Trạng thái hợp đồng không hợp lệ"
    ),

    SESSIONS_EXCEEDED(
            "BUSINESS",
            400,
            "CONTRACT.SESSIONS_EXCEEDED",
            "Số buổi tập đã sử dụng không thể vượt quá tổng số buổi của hợp đồng"
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    PtContractError(String type, int httpStatus, String code, String defaultMessage) {
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
