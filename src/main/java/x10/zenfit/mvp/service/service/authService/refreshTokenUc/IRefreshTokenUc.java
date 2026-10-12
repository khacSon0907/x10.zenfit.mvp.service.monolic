package x10.zenfit.mvp.service.service.authService.refreshTokenUc;

import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;

public interface IRefreshTokenUc {
    LoginResponse refreshToken(String refreshToken);
}
