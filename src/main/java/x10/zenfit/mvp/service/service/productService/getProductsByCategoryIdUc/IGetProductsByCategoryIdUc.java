package x10.zenfit.mvp.service.service.productService.getProductsByCategoryIdUc;

import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

import java.util.List;

public interface IGetProductsByCategoryIdUc {
    List<ProductEntity> getProductsByCategoryId(String categoryId);
}

