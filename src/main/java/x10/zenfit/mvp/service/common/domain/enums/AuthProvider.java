package x10.zenfit.mvp.service.common.domain.enums;

public enum AuthProvider {
    LOCAL,    // Đăng ký bằng email & password
    GOOGLE,   // Đăng nhập bằng Google
    LOCAL_GOOGLE, // Google account with password set
    GUEST  // ← Thêm provider cho guest use
}
