package x10.zenfit.mvp.service.service.roleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IRoleRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.RoleEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleResolver {

    public static final String DEFAULT_ROLE_CODE = "PT";

    private final IRoleRepository roleRepository;

    public List<String> getDefaultRoleIds() {

        return roleRepository.findByCode(DEFAULT_ROLE_CODE)
                .map(RoleEntity::getId)
                .map(List::of)
                .orElseThrow(() -> new IllegalStateException("Default PT role is not initialized"));
    }

    public List<String> resolveRoleCodes(List<String> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            return List.of(DEFAULT_ROLE_CODE);
        }

        List<String> roleCodes = roleRepository.findAllById(roleIds)
                .stream()
                .filter(RoleEntity::isActive)
                .map(RoleEntity::getCode)
                .map(String::trim)
                .filter(code -> !code.isEmpty())
                .toList();

        return roleCodes.isEmpty() ? List.of(DEFAULT_ROLE_CODE) : roleCodes;
    }
}
