package x10.zenfit.mvp.service.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    // Mặc định hỗ trợ cross-site cookie an toàn khi chạy trên cloud/production
    private static boolean secure = (System.getenv("RENDER") != null);
    private static String sameSite = (System.getenv("RENDER") != null) ? "None" : "Lax";

    // Spring inject giá trị từ application.yml (app.cookie.*) khi khởi động
    @Value("${app.cookie.secure:#{null}}")
    public void setSecure(Boolean secure) {
        if (secure != null) {
            CookieUtil.secure = secure;
        } else if (System.getenv("RENDER") != null) {
            CookieUtil.secure = true;
        }
    }

    @Value("${app.cookie.same-site:#{null}}")
    public void setSameSite(String sameSite) {
        if (sameSite != null && !sameSite.trim().isEmpty()) {
            CookieUtil.sameSite = sameSite;
        } else if (System.getenv("RENDER") != null) {
            CookieUtil.sameSite = "None";
        }
    }

    public static void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            long maxAgeSeconds
    ) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                buildCookie(name, value, maxAgeSeconds).toString()
        );
    }

    public static String getCookie(
            HttpServletRequest request,
            String name
    ) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public static void deleteCookie(
            HttpServletResponse response,
            String name
    ) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                buildCookie(name, "", 0).toString()
        );
    }

    // Dùng chung một chỗ để add và delete luôn khớp thuộc tính (secure, path, sameSite)
    private static ResponseCookie buildCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite(sameSite)
                .build();
    }
}