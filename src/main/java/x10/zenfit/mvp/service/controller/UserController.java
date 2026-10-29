package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.service.userService.ICoreUserService;
import x10.zenfit.mvp.service.service.userService.getAllUsersUc.GetAllUsersResponse;
import x10.zenfit.mvp.service.service.userService.getMeUc.GetMeResponse;
import x10.zenfit.mvp.service.service.userService.updateUserRolesUc.UpdateUserRolesRequest;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserRequest;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserResponse;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping({"/api/users", "/api/user"})
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

    @PutMapping( "/me/update")
    public ApiResponse<UpdateUserResponse> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        String userId = getCurrentUserId();
        UpdateUserResponse response = coreUserService.updateUser(userId, request);

        return ApiResponse.ok(
                "USER.UPDATE_SUCCESS",
                "Cập nhật thông tin người dùng thành công",
                response
        );
    }

    @GetMapping("/all")
    public ApiResponse<List<GetAllUsersResponse>> getAllUsers() {
        List<GetAllUsersResponse> response = coreUserService.getAllUsers();
        return ApiResponse.ok(
                "USER.GET_ALL_SUCCESS",
                "Lấy danh sách người dùng thành công",
                response
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<UpdateUserResponse> updateUserByAdmin(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        String adminId = getCurrentUserId();
        UpdateUserResponse response = coreUserService.updateUserByAdmin(id, adminId, request);

        return ApiResponse.ok(
                "USER.UPDATE_BY_ADMIN_SUCCESS",
                "Admin cập nhật thông tin người dùng thành công",
                response
        );
    }

    @PutMapping("/{id}/roles")
    public ApiResponse<UpdateUserResponse> updateUserRoles(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRolesRequest request
    ) {
        UpdateUserResponse response = coreUserService.updateUserRoles(id, request.getRoleIds());

        return ApiResponse.ok(
                "USER.UPDATE_ROLES_SUCCESS",
                "Cập nhật vai trò người dùng thành công",
                response
        );
    }

    @PutMapping("/{id}/lock")
    public ApiResponse<UpdateUserResponse> lockUser(@PathVariable String id) {
        String adminId = getCurrentUserId();
        UpdateUserResponse response = coreUserService.lockUser(id, adminId);

        return ApiResponse.ok(
                "USER.LOCK_SUCCESS",
                "Khóa tài khoản người dùng thành công",
                response
        );
    }

    @PutMapping("/{id}/unlock")
    public ApiResponse<UpdateUserResponse> unlockUser(@PathVariable String id) {
        String adminId = getCurrentUserId();
        UpdateUserResponse response = coreUserService.unlockUser(id, adminId);

        return ApiResponse.ok(
                "USER.UNLOCK_SUCCESS",
                "Mở khóa tài khoản người dùng thành công",
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
