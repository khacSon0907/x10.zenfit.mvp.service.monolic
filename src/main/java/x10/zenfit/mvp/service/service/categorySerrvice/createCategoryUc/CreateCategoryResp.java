package x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryResp {
    private String id;
    private String name;
    private String slug;
    private String description;
    private boolean active;
    private String bannerUrl;
    private String parentId;
    private Integer sortOrder;
    private List<CreateCategoryResp> children;
}
