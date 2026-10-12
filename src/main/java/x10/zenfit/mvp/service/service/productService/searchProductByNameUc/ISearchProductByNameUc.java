package x10.zenfit.mvp.service.service.productService.searchProductByNameUc;

import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

import java.util.List;

public interface ISearchProductByNameUc {
    List<ProductEntity> searchProductByName(String name);
}
