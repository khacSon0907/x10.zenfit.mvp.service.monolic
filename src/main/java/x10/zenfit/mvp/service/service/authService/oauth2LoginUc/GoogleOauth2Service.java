package x10.zenfit.mvp.service.service.authService.oauth2LoginUc;

import org.springframework.security.oauth2.core.user.OAuth2User;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;

public interface GoogleOauth2Service {
    LoginResponse loginWithGoogle(OAuth2User oauth2User);
}