package x10.zenfit.mvp.service.service.productService;

import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.service.productService.createProductUc.CreateProductRequest;
import x10.zenfit.mvp.service.service.productService.createProductUc.CreateProductResponse;
import x10.zenfit.mvp.service.service.productService.getAllProductsUc.ProductCursorPageResponse;
import x10.zenfit.mvp.service.service.productService.updateProductUc.UpdateProductRequest;
import x10.zenfit.mvp.service.service.productService.updateProductUc.UpdateProductResponse;

import java.util.List;

public interface ICoreProductService {
    CreateProductResponse createProduct(CreateProductRequest request);
    List<ProductEntity> getAllProducts();
    ProductCursorPageResponse getAllProductsByCursor(String cursor, Integer limit);
    List<ProductEntity> searchProductByName(String name);
    UpdateProductResponse updateProduct(UpdateProductRequest request);
    void deleteProduct(String id);
    ProductEntity getProductByIdOrSlug(String idOrSlug);
    List<ProductEntity> getProductsByCategoryId(String categoryId);
}
