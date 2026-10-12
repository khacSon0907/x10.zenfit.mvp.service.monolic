package x10.zenfit.mvp.service.service.authService.logoutUc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.config.jwt.IJwtService;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.util.CookieUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutUcImpl implements ILogoutUc {

    private final IJwtService jwtService;
    private final IRedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. Get access token from request header
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                // Check if token is valid and get remaining expiration
                if (!jwtService.isTokenExpired(token)) {
                    long remainingExpiration = jwtService.getRemainingExpiration(token);
                    
                    // Add token to blacklist
                    redisService.addToBlacklist(token, remainingExpiration);
                    log.info("Access token added to blacklist during logout");
                }
            } catch (Exception e) {
                log.warn("Invalid or expired token during logout: {}", e.getMessage());
                // Continue logout process even if token is invalid
            }
        }

        // 2. Clear refresh token cookie
        CookieUtil.deleteCookie(response, "refreshToken");
        log.info("Refresh token cookie cleared during logout");
    }
}
