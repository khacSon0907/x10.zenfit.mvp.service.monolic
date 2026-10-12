package x10.zenfit.mvp.service.common.domain.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    private String id;

    // ADMIN, STAFF, USER, MANAGER,...
    private String code;

    // Tên hiển thị
    private String name;

    private String description;

    private boolean active;
}