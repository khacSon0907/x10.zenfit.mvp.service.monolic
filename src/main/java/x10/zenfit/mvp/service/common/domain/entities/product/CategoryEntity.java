package x10.zenfit.mvp.service.common.domain.entities.product;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class CategoryEntity {

    private String id;

    private String name;

    private String description;

    private String slug;

    // active / hidden
    private boolean active;

    // Banner của danh mục
    private String bannerUrl;

    // null = danh mục gốc
    private String parentId;

    private Integer sortOrder;

    private Instant createdAt;
    private Instant updatedAt;
}
