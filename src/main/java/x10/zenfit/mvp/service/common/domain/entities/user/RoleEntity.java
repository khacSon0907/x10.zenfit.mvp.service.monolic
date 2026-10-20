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

    // ADMIN, HEAD_OF_TRAINING, TEAM_MANAGER, PT
    private String code;

    // Tên hiển thị
    private String name;

    private String description;

    private boolean active;
}