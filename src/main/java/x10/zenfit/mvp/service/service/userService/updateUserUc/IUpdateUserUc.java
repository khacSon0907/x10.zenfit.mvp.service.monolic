package x10.zenfit.mvp.service.service.userService.updateUserUc;

public interface IUpdateUserUc {
    UpdateUserResponse updateUser(String userId, UpdateUserRequest request);
    UpdateUserResponse updateUserByAdmin(String userId, String adminId, UpdateUserRequest request);
    UpdateUserResponse updateUserStatusByAdmin(String userId, String adminId, boolean locked);
}

