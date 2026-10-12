package x10.zenfit.mvp.service.service.authService;


import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.authService.registerUc.IRegisterUc;
import x10.zenfit.mvp.service.service.authService.registerUc.RegisterResponse;
import x10.zenfit.mvp.service.service.authService.verifyOtpUc.IVerifyOtpUc;
import x10.zenfit.mvp.service.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.zenfit.mvp.service.service.authService.loginUc.ILoginUc;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginReq;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;
import x10.zenfit.mvp.service.service.authService.logoutUc.ILogoutUc;
import x10.zenfit.mvp.service.service.authService.refreshTokenUc.IRefreshTokenUc;
import x10.zenfit.mvp.service.service.authService.forgotPasswordUc.ForgotPasswordReq;
import x10.zenfit.mvp.service.service.authService.forgotPasswordUc.IForgotPasswordUc;
import x10.zenfit.mvp.service.service.authService.changePasswordUc.ChangePasswordReq;
import x10.zenfit.mvp.service.service.authService.resetPasswordUc.ResetPasswordReq;
import x10.zenfit.mvp.service.service.authService.verifyForgotPasswordOtpUc.IVerifyForgotPasswordOtpUc;
import x10.zenfit.mvp.service.service.authService.resetPasswordUc.IResetPasswordUc;
import x10.zenfit.mvp.service.service.authService.changePasswordUc.IChangePasswordUc;
import x10.zenfit.mvp.service.service.authService.oauth2LoginUc.GoogleOauth2Service;
import x10.zenfit.mvp.service.config.jwt.JwtProperties;
import x10.zenfit.mvp.service.util.CookieUtil;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;
import x10.zenfit.mvp.service.service.userService.createUserUc.CreateUserReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import x10.zenfit.mvp.service.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpReq;
import x10.zenfit.mvp.service.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpResponse;

@Service
@AllArgsConstructor
public class CoreAuthServiceImpl implements ICoreAuthService {

    private final IRegisterUc registerUc;
    private final IVerifyOtpUc verifyOtpUc;
    private final ILoginUc loginUc;
    private final ILogoutUc logoutUc;
    private final IRefreshTokenUc refreshTokenUc;
    private final IForgotPasswordUc forgotPasswordUc;
    private final IVerifyForgotPasswordOtpUc verifyForgotPasswordOtpUc;
    private final IResetPasswordUc resetPasswordUc;
    private final IChangePasswordUc changePasswordUc;
    private final GoogleOauth2Service googleOauth2Service;
    private final JwtProperties jwtProperties;

    @Override
    public RegisterResponse register(CreateUserReq  req) {
        return registerUc.register(req);
    }

    @Override
    public void verifyOtp(VerifyOtpReq req) {
        verifyOtpUc.verifyOtp(req);
    }

    @Override
    public LoginResponse login(LoginReq req) {
        return loginUc.login(req);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        logoutUc.logout(request, response);
    }

    @Override
    public LoginResponse refreshToken(String bodyToken, HttpServletRequest request, HttpServletResponse response) {
        // 1. Cố gắng lấy refresh token từ Cookie
        String refreshToken = CookieUtil.getCookie(request, "refreshToken");

        // 2. Nếu không có ở Cookie, lấy từ request body làm phương án dự phòng (fallback)
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            refreshToken = bodyToken;
        }

        // 3. Nếu vẫn trống, ném lỗi không hợp lệ
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 4. Thực thi Use Case làm mới token và nhận các token mới
        LoginResponse loginResponse = refreshTokenUc.refreshToken(refreshToken);

        // 5. Tính toán hạn dùng tối đa (max-age) bằng giây từ cấu hình Refresh Token Expiration (milis)
        long maxAgeSeconds = jwtProperties.getRefreshTokenExpiration() / 1000;

        // 6. Ghi đè Cookie refreshToken mới
        CookieUtil.addCookie(response, "refreshToken", loginResponse.getRefreshToken(), maxAgeSeconds);

        return loginResponse;
    }

    @Override
    public void forgotPassword(ForgotPasswordReq req) {
        forgotPasswordUc.forgotPassword(req);
    }

    @Override
    public VerifyForgotPasswordOtpResponse verifyForgotPasswordOtp(
            VerifyForgotPasswordOtpReq req
    ) {
        return verifyForgotPasswordOtpUc.verifyOtp(req);
    }

    @Override
    public void resetPassword(ResetPasswordReq req) {
        resetPasswordUc.resetPassword(req);
    }

    @Override
    public void changePassword(String userId, ChangePasswordReq req) {
        changePasswordUc.changePassword(userId, req);
    }

    @Override
    public LoginResponse loginWithGoogle(OAuth2User req) {
        return googleOauth2Service.loginWithGoogle(req);
    }
}
