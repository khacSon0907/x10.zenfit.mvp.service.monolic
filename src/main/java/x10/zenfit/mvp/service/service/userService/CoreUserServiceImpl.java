package x10.zenfit.mvp.service.service.userService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.service.userService.createUserUc.CreateUserReq;
import x10.zenfit.mvp.service.service.userService.createUserUc.ICreateUserUc;
import x10.zenfit.mvp.service.service.userService.getAllUsersUc.GetAllUsersResponse;
import x10.zenfit.mvp.service.service.userService.getAllUsersUc.IGetAllUsersUc;
import x10.zenfit.mvp.service.service.userService.getMeUc.IGetMeUc;
import x10.zenfit.mvp.service.service.userService.getMeUc.GetMeResponse;
import x10.zenfit.mvp.service.service.userService.updateUserUc.IUpdateUserUc;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserRequest;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserResponse;
import x10.zenfit.mvp.service.service.userService.updateUserRolesUc.IUpdateUserRolesUc;

import java.util.List;

@Service
@AllArgsConstructor
public class CoreUserServiceImpl implements ICoreUserService{

    private final ICreateUserUc createUserUC;
    private final IGetMeUc getMeUc;
    private final IUpdateUserUc updateUserUc;
    private final IUpdateUserRolesUc updateUserRolesUc;
    private final IGetAllUsersUc getAllUsersUc;

    @Override
    public UserEntity createUser(CreateUserReq user) {
        return createUserUC.createUser(user);
    }

    @Override
    public GetMeResponse getMe(String userId) {
        return getMeUc.getMe(userId);
    }

    @Override
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        return updateUserUc.updateUser(userId, request);
    }

    @Override
    public UpdateUserResponse updateUserByAdmin(String userId, String adminId, UpdateUserRequest request) {
        return updateUserUc.updateUserByAdmin(userId, adminId, request);
    }

    @Override
    public UpdateUserResponse updateUserRoles(String userId, List<String> roleIds) {
        return updateUserRolesUc.updateUserRoles(userId, roleIds);
    }

    @Override
    public UpdateUserResponse lockUser(String userId, String adminId) {
        return updateUserUc.updateUserStatusByAdmin(userId, adminId, true);
    }

    @Override
    public UpdateUserResponse unlockUser(String userId, String adminId) {
        return updateUserUc.updateUserStatusByAdmin(userId, adminId, false);
    }

    @Override
    public List<GetAllUsersResponse> getAllUsers() {
        return getAllUsersUc.getAllUsers();
    }
}
