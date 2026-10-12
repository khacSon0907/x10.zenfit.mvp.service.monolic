package x10.zenfit.mvp.service.service.roleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IRoleRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.RoleEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.role.RoleError;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CoreRoleServiceImpl implements ICoreRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleRequest request) {

        String code = normalizeCode(request.getCode());
        validateCode(code);

        if (roleRepository.existsByCode(code)) {
            throw new BusinessException(RoleError.ROLE_EXISTS);
        }

        RoleEntity role = RoleEntity.builder()
                .code(code)
                .name(request.getName().trim())
                .description(request.getDescription())
                .active(request.getActive() == null || request.getActive())
                .build();

        return toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(String id, RoleRequest request) {

        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(RoleError.ROLE_NOT_FOUND));

        String code = normalizeCode(request.getCode());
        validateCode(code);

        roleRepository.findByCode(code)
                .filter(existingRole -> !existingRole.getId().equals(id))
                .ifPresent(existingRole -> {
                    throw new BusinessException(RoleError.ROLE_EXISTS);
                });

        role.setCode(code);
        role.setName(request.getName().trim());
        role.setDescription(request.getDescription());
        role.setActive(request.getActive() == null || request.getActive());

        return toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse getRoleById(String id) {

        return roleRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException(RoleError.ROLE_NOT_FOUND));
    }

    @Override
    public List<RoleResponse> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RoleResponse toResponse(RoleEntity role) {

        return RoleResponse.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .description(role.getDescription())
                .active(role.isActive())
                .build();
    }

    private String normalizeCode(String code) {

        if (code == null) {
            return "";
        }

        String normalizedCode = code.trim().toUpperCase(Locale.ROOT);

        if (normalizedCode.startsWith("ROLE_")) {
            return normalizedCode.substring("ROLE_".length());
        }

        return normalizedCode;
    }

    private void validateCode(String code) {

        if (code.isBlank() || !code.matches("^[A-Z][A-Z0-9_]{1,49}$")) {
            throw new BusinessException(RoleError.ROLE_INVALID_CODE);
        }
    }
}
