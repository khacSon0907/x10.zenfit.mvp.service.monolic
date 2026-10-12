package x10.zenfit.mvp.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.config.jwt.JwtProperties;
import x10.zenfit.mvp.service.service.authService.ICoreAuthService;
import x10.zenfit.mvp.service.service.authService.changePasswordUc.ChangePasswordReq;
import x10.zenfit.mvp.service.service.authService.forgotPasswordUc.ForgotPasswordReq;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginReq;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;
import x10.zenfit.mvp.service.service.authService.oauth2LoginUc.OAuth2ExchangeRequest;
import x10.zenfit.mvp.service.service.authService.oauth2LoginUc.OAuth2LoginCodeService;
import x10.zenfit.mvp.service.service.authService.refreshTokenUc.RefreshTokenReq;
import x10.zenfit.mvp.service.service.authService.registerUc.RegisterResponse;
import x10.zenfit.mvp.service.service.authService.resetPasswordUc.ResetPasswordReq;
import x10.zenfit.mvp.service.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpReq;
import x10.zenfit.mvp.service.service.authService.verifyForgotPasswordOtpUc.VerifyForgotPasswordOtpResponse;
import x10.zenfit.mvp.service.service.authService.verifyOtpUc.VerifyOtpReq;
import x10.zenfit.mvp.service.service.userService.createUserUc.CreateUserReq;
import x10.zenfit.mvp.service.share.response.ApiResponse;
import x10.zenfit.mvp.service.util.CookieUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final ICoreAuthService coreAuthService;
    private final JwtProperties jwtProperties;
    private final OAuth2LoginCodeService oAuth2LoginCodeService;

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<LoginResponse> login(
//            @Valid @RequestBody LoginReq req,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        LoginResponse loginResponse = coreAuthService.login(req);
//
//        addRefreshTokenCookie(response, loginResponse);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.LOGIN_SUCCESS",
//                "Đăng nhập thành công",
//                loginResponse,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ApiResponse<RegisterResponse> register(
//            @Valid @RequestBody CreateUserReq req,
//            HttpServletRequest request
//    ) {
//        RegisterResponse response = coreAuthService.register(req);
//
//        return ApiResponse.success(
//                201,
//                "AUTH.REGISTER_SUCCESS",
//                "Đăng ký thành công",
//                response,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/verify-otp")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<Void> verifyOtp(
//            @Valid @RequestBody VerifyOtpReq req,
//            HttpServletRequest request
//    ) {
//        coreAuthService.verifyOtp(req);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.VERIFY_OTP_SUCCESS",
//                "Xác thực email thành công",
//                null,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/logout")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<Void> logout(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        coreAuthService.logout(request, response);
//
//        CookieUtil.deleteCookie(response, REFRESH_TOKEN_COOKIE_NAME);
//        SecurityContextHolder.clearContext();
//
//        return ApiResponse.success(
//                200,
//                "AUTH.LOGOUT_SUCCESS",
//                "Đăng xuất thành công",
//                null,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/refresh")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<LoginResponse> refreshToken(
//            @RequestBody(required = false) RefreshTokenReq req,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        String bodyToken = (req != null) ? req.getRefreshToken() : null;
//
//        LoginResponse loginResponse = coreAuthService.refreshToken(
//                bodyToken,
//                request,
//                response
//        );
//
//        addRefreshTokenCookie(response, loginResponse);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.REFRESH_SUCCESS",
//                "Làm mới token thành công",
//                loginResponse,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/oauth2/exchange")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<LoginResponse> exchangeOAuth2Code(
//            @Valid @RequestBody OAuth2ExchangeRequest req,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        LoginResponse loginResponse = oAuth2LoginCodeService.consumeCode(req.getCode());
//
//        addRefreshTokenCookie(response, loginResponse);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.OAUTH2_EXCHANGE_SUCCESS",
//                "Đăng nhập Google thành công",
//                loginResponse,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/forgot-password")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<Void> forgotPassword(
//            @Valid @RequestBody ForgotPasswordReq req,
//            HttpServletRequest request
//    ) {
//        coreAuthService.forgotPassword(req);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.FORGOT_PASSWORD_SUCCESS",
//                "Mã xác nhận quên mật khẩu đã được gửi đến email của bạn",
//                null,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/verify-forgot-password-otp")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<VerifyForgotPasswordOtpResponse> verifyForgotPasswordOtp(
//            @Valid @RequestBody VerifyForgotPasswordOtpReq req,
//            HttpServletRequest request
//    ) {
//        VerifyForgotPasswordOtpResponse response = coreAuthService.verifyForgotPasswordOtp(req);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.VERIFY_FORGOT_PASSWORD_OTP_SUCCESS",
//                "Xác thực mã OTP thành công",
//                response,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/reset-password")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<Void> resetPassword(
//            @Valid @RequestBody ResetPasswordReq req,
//            HttpServletRequest request
//    ) {
//        coreAuthService.resetPassword(req);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.RESET_PASSWORD_SUCCESS",
//                "Đặt lại mật khẩu thành công",
//                null,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    @PostMapping("/change-password")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<Void> changePassword(
//            @Valid @RequestBody ChangePasswordReq req,
//            HttpServletRequest request
//    ) {
//        String userId = getCurrentUserId();
//
//        coreAuthService.changePassword(userId, req);
//
//        return ApiResponse.success(
//                200,
//                "AUTH.CHANGE_PASSWORD_SUCCESS",
//                "Đổi mật khẩu thành công",
//                null,
//                request.getRequestURI(),
//                null
//        );
//    }
//
//    private void addRefreshTokenCookie(
//            HttpServletResponse response,
//            LoginResponse loginResponse
//    ) {
//        if (loginResponse == null || loginResponse.getRefreshToken() == null) {
//            return;
//        }
//
//        long maxAgeSeconds = jwtProperties.getRefreshTokenExpiration() / 1000;
//
//        CookieUtil.addCookie(
//                response,
//                REFRESH_TOKEN_COOKIE_NAME,
//                loginResponse.getRefreshToken(),
//                maxAgeSeconds
//        );
//    }
//
//    private String getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null ||
//                !authentication.isAuthenticated() ||
//                authentication.getPrincipal() == null ||
//                "anonymousUser".equals(authentication.getPrincipal())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
//        }
//
//        return authentication.getPrincipal().toString();
//    }
}
