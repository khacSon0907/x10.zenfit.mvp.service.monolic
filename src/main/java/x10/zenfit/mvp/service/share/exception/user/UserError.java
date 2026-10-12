package x10.zenfit.mvp.service.share.exception.user;

import x10.zenfit.mvp.service.share.exception.ErrorDescriptor;

public enum UserError implements ErrorDescriptor {

    EMAIL_EXISTS(
            "BUSINESS",
            409,
            "USER.EMAIL_EXISTS",
            "Email đã tồn tại"
    ),

    USER_NOT_FOUND(
            "BUSINESS",
            404,
            "USER.NOT_FOUND",
            "Không tìm thấy người dùng"
    ),

    BIRTHDAY_UPDATE_LIMIT(
            "BUSINESS",
            400,
            "USER.BIRTHDAY_UPDATE_LIMIT",
            "Ban chi duoc thay doi ngay sinh 1 lan moi nam. Neu can thay doi them, vui long lien he bo phan Cham soc khach hang."
    ),

    INVALID_OTP(
            "BUSINESS",
            400,
            "AUTH.INVALID_OTP",
            "OTP không hợp lệ hoặc đã hết hạn"
    ),

    INVALID_CREDENTIALS(
            "BUSINESS",
            400,
            "AUTH.INVALID_CREDENTIALS",
            "Tài khoản hoặc mật khẩu không chính xác"
    ),

    USER_NOT_ACTIVE(
            "BUSINESS",
            403,
            "AUTH.USER_NOT_ACTIVE",
            "Tài khoản chưa được kích hoạt hoặc đã bị khóa"
    ),

    EMAIL_NOT_VERIFIED(
            "BUSINESS",
            403,
            "AUTH.EMAIL_NOT_VERIFIED",
            "Email chưa được xác thực"
    ),

    LOGIN_LOCKED(
            "BUSINESS",
            429,
            "AUTH.LOGIN_LOCKED",
            "Tài khoản đã bị khóa tạm thời do nhập sai mật khẩu quá 5 lần. Vui lòng thử lại sau 5 phút."
    ),

    INVALID_REFRESH_TOKEN(
            "BUSINESS",
            401,
            "AUTH.INVALID_REFRESH_TOKEN",
            "Refresh token không hợp lệ hoặc đã hết hạn"
    ),

    RATE_LIMIT_EXCEEDED(
            "BUSINESS",
            429,
            "AUTH.RATE_LIMIT_EXCEEDED",
            "Bạn thao tác quá nhanh, vui lòng thử lại sau"
    ),

    INVALID_RESET_TOKEN(
            "BUSINESS",
            400,
            "AUTH.INVALID_RESET_TOKEN",
            "Mã xác nhận không hợp lệ hoặc đã hết hạn"
    ),

    WRONG_PASSWORD(
            "BUSINESS",
            400,
            "USER.WRONG_PASSWORD",
            "Mật khẩu hiện tại không chính xác"
    ),

    PASSWORD_MISMATCH(
            "BUSINESS",
            400,
            "USER.PASSWORD_MISMATCH",
            "Xác nhận mật khẩu không khớp"
    ),

    SAME_PASSWORD(
            "BUSINESS",
            400,
            "USER.SAME_PASSWORD",
            "Mật khẩu mới không được trùng với mật khẩu hiện tại"
    ),

    INVALID_GOOGLE_TOKEN(
            "BUSINESS",
            401,
            "AUTH.INVALID_GOOGLE_TOKEN",
            "Google token không hợp lệ hoặc đã hết hạn"
    ),

    INVALID_OAUTH2_CODE(
            "BUSINESS",
            401,
            "AUTH.INVALID_OAUTH2_CODE",
            "Mã đăng nhập Google không hợp lệ hoặc đã hết hạn"
    ),

    EMAIL_NOT_VERIFIED_GOOGLE(
            "BUSINESS",
            403,
            "AUTH.EMAIL_NOT_VERIFIED_GOOGLE",
            "Email trên tài khoản Google chưa được xác thực"
    ),

    GOOGLE_ACCOUNT_LOGIN_REQUIRED(
            "BUSINESS",
            403,
            "AUTH.GOOGLE_ACCOUNT_LOGIN_REQUIRED",
            "Tài khoản này được tạo bằng Google. Vui lòng đăng nhập bằng Google hoặc đặt mật khẩu trước."
    ),

    GUEST_ACCOUNT_CANNOT_LOGIN(
            "BUSINESS",
            403,
            "AUTH.GUEST_ACCOUNT_CANNOT_LOGIN",
            "Tài khoản khách không thể đăng nhập bằng mật khẩu. Vui lòng đăng ký tài khoản."
    );

    private final String type;
    private final int httpStatus;
    private final String code;
    private final String defaultMessage;

    UserError(String type, int httpStatus, String code, String defaultMessage) {
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
