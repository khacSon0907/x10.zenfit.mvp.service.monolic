package x10.zenfit.mvp.service.service.roleService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private String id;

    private String code;

    private String name;

    private String description;

    private boolean active;
}
