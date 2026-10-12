package x10.zenfit.mvp.service.service.productService.getAllProductsUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCursorPageResponse {

    private List<ProductEntity> items;

    private String nextCursor;

    private boolean hasNext;

    private int limit;
}
