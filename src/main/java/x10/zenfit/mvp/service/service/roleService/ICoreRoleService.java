package x10.zenfit.mvp.service.service.roleService;

import java.util.List;

public interface ICoreRoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRole(String id, RoleRequest request);

    RoleResponse getRoleById(String id);

    List<RoleResponse> getAllRoles();
}
