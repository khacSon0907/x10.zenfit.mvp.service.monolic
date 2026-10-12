package x10.zenfit.mvp.service.share.exception.voucher;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum VoucherError implements ErrorDescriptor {
    VOUCHER_NOT_FOUND("BUSINESS", 404, "VOUCHER.NOT_FOUND", "Khong tim thay voucher"),
    VOUCHER_CODE_EXISTS("BUSINESS", 409, "VOUCHER.CODE_EXISTS", "Ma voucher da ton tai"),
    VOUCHER_INVALID("BUSINESS", 400, "VOUCHER.INVALID", "Voucher khong hop le"),
    VOUCHER_INACTIVE("BUSINESS", 400, "VOUCHER.INACTIVE", "Voucher dang bi tat"),
    VOUCHER_NOT_STARTED("BUSINESS", 400, "VOUCHER.NOT_STARTED", "Voucher chua den thoi gian su dung"),
    VOUCHER_EXPIRED("BUSINESS", 400, "VOUCHER.EXPIRED", "Voucher da het han"),
    VOUCHER_USAGE_LIMIT_REACHED("BUSINESS", 400, "VOUCHER.USAGE_LIMIT_REACHED", "Voucher da het luot su dung"),
    VOUCHER_USER_NOT_ALLOWED("BUSINESS", 403, "VOUCHER.USER_NOT_ALLOWED", "Voucher khong thuoc ve user nay"),
    VOUCHER_MIN_ORDER_NOT_MET("BUSINESS", 400, "VOUCHER.MIN_ORDER_NOT_MET", "Don hang chua dat gia tri toi thieu de dung voucher"),
    BIRTHDAY_NOT_TODAY("BUSINESS", 400, "VOUCHER.BIRTHDAY_NOT_TODAY", "Hom nay khong phai sinh nhat cua user"),
    BIRTHDAY_MISSING("BUSINESS", 400, "VOUCHER.BIRTHDAY_MISSING", "User chua co ngay sinh");

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    VoucherError(String type, int httpStatus, String code, String defaultMessage) {
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
