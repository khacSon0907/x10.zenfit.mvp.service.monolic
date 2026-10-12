package x10.zenfit.mvp.service.service.productService.getAllProductsUc;

import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import java.util.List;

public interface IGetAllProductsUc {
    List<ProductEntity> getAllProducts();

    ProductCursorPageResponse getAllProductsByCursor(String cursor, Integer limit);
}
