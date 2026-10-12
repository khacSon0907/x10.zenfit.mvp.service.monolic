package x10.zenfit.mvp.service.service.userService;

import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.service.userService.createUserUc.CreateUserReq;
import x10.zenfit.mvp.service.service.userService.getAllUsersUc.GetAllUsersResponse;
import x10.zenfit.mvp.service.service.userService.getMeUc.GetMeResponse;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserRequest;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserResponse;

import java.util.List;

public interface ICoreUserService {
    UserEntity createUser(CreateUserReq user);

    GetMeResponse getMe(String userId);

    UpdateUserResponse updateUser(String userId, UpdateUserRequest request);

    UpdateUserResponse updateUserByAdmin(String userId, String adminId, UpdateUserRequest request);

    UpdateUserResponse updateUserRoles(String userId, List<String> roleIds);

    UpdateUserResponse lockUser(String userId, String adminId);

    UpdateUserResponse unlockUser(String userId, String adminId);

    List<GetAllUsersResponse> getAllUsers();
}
