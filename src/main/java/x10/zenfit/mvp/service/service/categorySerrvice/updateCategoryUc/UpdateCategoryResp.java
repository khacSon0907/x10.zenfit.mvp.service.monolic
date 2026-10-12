package x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateCategoryResp {

    private boolean active;
    private String description;
    private String slug;
    private String name;
    private String id;
    private String bannerUrl;
    private String parentId;
    private Integer sortOrder;
}
