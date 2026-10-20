package x10.zenfit.mvp.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.service.userService.ICoreUserService;
import x10.zenfit.mvp.service.service.userService.getMeUc.GetMeResponse;
import x10.zenfit.mvp.service.share.response.ApiResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final ICoreUserService coreUserService;

    @GetMapping("/me")
    public ApiResponse<GetMeResponse> getMe() {
        String userId = getCurrentUserId();

        GetMeResponse response = coreUserService.getMe(userId);

        return ApiResponse.ok(
                "USER.GET_ME_SUCCESS",
                "Lấy thông tin người dùng thành công",
                response
        );
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal() == null ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        return authentication.getPrincipal().toString();
    }
}