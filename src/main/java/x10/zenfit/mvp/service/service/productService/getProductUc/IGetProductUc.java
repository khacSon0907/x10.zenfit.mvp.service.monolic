package x10.zenfit.mvp.service.service.productService.getProductUc;

import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

public interface IGetProductUc {
    ProductEntity getProductByIdOrSlug(String idOrSlug);
}
