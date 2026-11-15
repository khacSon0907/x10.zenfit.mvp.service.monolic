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

    // Giá trị mặc định an toàn cho môi trường dev/test khi Spring chưa inject
    private static boolean secure = false;
    private static String sameSite = "Lax";

    // Spring inject giá trị từ application.yml (app.cookie.*) khi khởi động
    @Value("${app.cookie.secure:false}")
    public void setSecure(boolean secure) {
        CookieUtil.secure = secure;
    }

    @Value("${app.cookie.same-site:Lax}")
    public void setSameSite(String sameSite) {
        CookieUtil.sameSite = sameSite;
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