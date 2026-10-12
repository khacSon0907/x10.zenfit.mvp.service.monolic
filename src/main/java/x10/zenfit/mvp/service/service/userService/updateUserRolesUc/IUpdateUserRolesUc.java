package x10.zenfit.mvp.service.service.userService.updateUserRolesUc;

import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserResponse;

import java.util.List;

public interface IUpdateUserRolesUc {

    UpdateUserResponse updateUserRoles(String userId, List<String> roleIds);
}
