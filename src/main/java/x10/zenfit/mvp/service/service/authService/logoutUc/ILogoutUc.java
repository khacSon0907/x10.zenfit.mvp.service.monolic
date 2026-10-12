package x10.zenfit.mvp.service.service.authService.logoutUc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ILogoutUc {
    void logout(HttpServletRequest request, HttpServletResponse response);
}
