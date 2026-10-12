package x10.zenfit.mvp.service.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.config.jwt.JwtProperties;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;
import x10.zenfit.mvp.service.service.authService.oauth2LoginUc.GoogleOauth2Service;
import x10.zenfit.mvp.service.service.authService.oauth2LoginUc.OAuth2LoginCodeService;
import x10.zenfit.mvp.service.util.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final GoogleOauth2Service googleOauth2Service;
    private final OAuth2LoginCodeService oAuth2LoginCodeService;
    private final JwtProperties jwtProperties;

    @Value("${app.frontend-url:https://polo-man.vercel.app}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        LoginResponse loginResponse = googleOauth2Service.loginWithGoogle(oauth2User);

        long maxAgeSeconds = jwtProperties.getRefreshTokenExpiration() / 1000;

        CookieUtil.addCookie(
                response,
                REFRESH_TOKEN_COOKIE_NAME,
                loginResponse.getRefreshToken(),
                maxAgeSeconds
        );

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        invalidateOAuth2Session(request);
        response.sendRedirect(buildSuccessRedirectUrl(loginResponse));
    }

    private void invalidateOAuth2Session(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    private String buildSuccessRedirectUrl(LoginResponse loginResponse) {
        String code = oAuth2LoginCodeService.createCode(loginResponse);

        return frontendUrl + "/oauth2/success"
                + "?code=" + code;
    }
}
