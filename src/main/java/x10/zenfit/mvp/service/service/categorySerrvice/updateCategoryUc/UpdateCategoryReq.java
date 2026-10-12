package x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class UpdateCategoryReq {

    private String id;

    @NotBlank(message = "Category name khong duoc de trong")
    @Size(min = 1, max = 255, message = "Category name phai tu 1 den 255 ky tu")
    private String name;

    @Size(max = 1000, message = "Description khong duoc vuot qua 1000 ky tu")
    private String description;

    private String bannerUrl;

    private Boolean active;

    private String parentId;

    private Integer sortOrder;
}
