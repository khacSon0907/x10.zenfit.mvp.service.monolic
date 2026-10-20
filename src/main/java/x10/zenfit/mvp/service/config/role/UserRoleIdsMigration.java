package x10.zenfit.mvp.service.config.role;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.infrastructure.role.db.mongodb.RoleDocument;
import x10.zenfit.mvp.service.infrastructure.role.db.mongodb.RoleMongoRepository;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserDocument;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserMongoRepository;

import x10.zenfit.mvp.service.service.roleService.RoleResolver;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(2)
@RequiredArgsConstructor
public class UserRoleIdsMigration implements CommandLineRunner {

    private final UserMongoRepository userMongoRepository;
    private final RoleMongoRepository roleMongoRepository;

    @Override
    public void run(String... args) {

        Map<String, RoleDocument> rolesByCode = roleMongoRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        role -> normalizeRoleCode(role.getCode()),
                        Function.identity(),
                        (existingRole, duplicateRole) -> existingRole
                ));

        RoleDocument defaultRole = rolesByCode.get(RoleResolver.DEFAULT_ROLE_CODE);
        if (defaultRole == null) {
            throw new IllegalStateException("Default PT role is not initialized");
        }

        RoleDocument legacyUserRole = rolesByCode.get("USER");

        userMongoRepository.findAll()
                .stream()
                .filter(user -> needsMigration(user, legacyUserRole))
                .map(user -> migrateUserRoleIds(user, rolesByCode, defaultRole, legacyUserRole))
                .forEach(userMongoRepository::save);
    }

    private boolean needsMigration(UserDocument user, RoleDocument legacyUserRole) {

        if (user.getRoleIds() == null || user.getRoleIds().isEmpty()) {
            return true;
        }

        return legacyUserRole != null && user.getRoleIds().contains(legacyUserRole.getId());
    }

    private UserDocument migrateUserRoleIds(
            UserDocument user,
            Map<String, RoleDocument> rolesByCode,
            RoleDocument defaultRole,
            RoleDocument legacyUserRole
    ) {

        List<String> roleIds = resolveRoleIds(user.getLegacyRoles(), rolesByCode);

        if (roleIds.isEmpty()) {
            if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
                roleIds = user.getRoleIds().stream()
                        .map(id -> (legacyUserRole != null && Objects.equals(id, legacyUserRole.getId()))
                                ? defaultRole.getId()
                                : id)
                        .distinct()
                        .toList();
            } else {
                roleIds = List.of(defaultRole.getId());
            }
        }

        user.setRoleIds(roleIds);
        user.setLegacyRoles(null);

        return user;
    }

    private List<String> resolveRoleIds(List<String> legacyRoles, Map<String, RoleDocument> rolesByCode) {

        if (legacyRoles == null || legacyRoles.isEmpty()) {
            return List.of();
        }

        return legacyRoles.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeRoleCode)
                .filter(role -> !role.isEmpty())
                .map(roleCode -> {
                    if ("USER".equalsIgnoreCase(roleCode)) {
                        return rolesByCode.get(RoleResolver.DEFAULT_ROLE_CODE);
                    }
                    return rolesByCode.get(roleCode);
                })
                .filter(Objects::nonNull)
                .map(RoleDocument::getId)
                .toList();
    }

    private String normalizeRoleCode(String roleCode) {

        if (roleCode == null) {
            return "";
        }

        String normalizedRoleCode = roleCode.trim().toUpperCase(Locale.ROOT);

        if (normalizedRoleCode.startsWith("ROLE_")) {
            return normalizedRoleCode.substring("ROLE_".length());
        }

        return normalizedRoleCode;
    }
}
