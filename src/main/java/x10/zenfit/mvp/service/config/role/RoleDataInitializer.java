
package x10.zenfit.mvp.service.config.role;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IRoleRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.RoleEntity;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final IRoleRepository roleRepository;

    @Override
    public void run(String... args) {

        List<RoleEntity> defaultRoles = List.of(
                RoleEntity.builder()
                        .code("ADMIN")
                        .name("Admin")
                        .description(
                                "Quản trị tài khoản, phân quyền và cấu hình hệ thống ZenFit"
                        )
                        .active(true)
                        .build(),

                RoleEntity.builder()
                        .code("HEAD_OF_TRAINING")
                        .name("Quản lý huấn luyện")
                        .description(
                                "Quản lý toàn bộ Team Manager, giám sát các đội ngũ PT và theo dõi báo cáo tổng hợp"
                        )
                        .active(true)
                        .build(),

                RoleEntity.builder()
                        .code("TEAM_MANAGER")
                        .name("Quản lý đội ngũ PT")
                        .description(
                                "Quản lý PT trong team, phân công công việc và theo dõi hiệu suất đội ngũ"
                        )
                        .active(true)
                        .build(),

                RoleEntity.builder()
                        .code("PT")
                        .name("Personal Trainer")
                        .description(
                                "Quản lý lịch làm việc, học viên được phân công và kết quả huấn luyện"
                        )
                        .active(true)
                        .build()
        );

        defaultRoles.forEach(defaultRole ->
                roleRepository.findByCode(defaultRole.getCode())
                        .ifPresentOrElse(
                                existing -> {
                                    existing.setName(defaultRole.getName());
                                    existing.setDescription(defaultRole.getDescription());
                                    existing.setActive(true);
                                    roleRepository.save(existing);
                                },
                                () -> roleRepository.save(defaultRole)
                        )
        );
    }
}
