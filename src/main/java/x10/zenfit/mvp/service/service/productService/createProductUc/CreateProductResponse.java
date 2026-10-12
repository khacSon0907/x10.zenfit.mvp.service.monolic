package x10.zenfit.mvp.service.service.productService.createProductUc;

import lombok.Builder;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreateProductResponse {
    private String id;
    private String name;
    private String slug;
    private String categoryId;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Boolean active;
    private List<ColorVariantEntity> colors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
