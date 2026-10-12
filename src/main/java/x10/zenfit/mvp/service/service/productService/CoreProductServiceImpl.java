package x10.zenfit.mvp.service.service.productService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.service.productService.createProductUc.CreateProductRequest;
import x10.zenfit.mvp.service.service.productService.createProductUc.CreateProductResponse;
import x10.zenfit.mvp.service.service.productService.createProductUc.ICreateProductUc;
import x10.zenfit.mvp.service.service.productService.deleteProductUc.IDeleteProductUc;
import x10.zenfit.mvp.service.service.productService.getAllProductsUc.IGetAllProductsUc;
import x10.zenfit.mvp.service.service.productService.getAllProductsUc.ProductCursorPageResponse;
import x10.zenfit.mvp.service.service.productService.getProductUc.IGetProductUc;
import x10.zenfit.mvp.service.service.productService.getProductsByCategoryIdUc.IGetProductsByCategoryIdUc;
import x10.zenfit.mvp.service.service.productService.searchProductByNameUc.ISearchProductByNameUc;
import x10.zenfit.mvp.service.service.productService.updateProductUc.IUpdateProductUc;
import x10.zenfit.mvp.service.service.productService.updateProductUc.UpdateProductRequest;
import x10.zenfit.mvp.service.service.productService.updateProductUc.UpdateProductResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreProductServiceImpl implements ICoreProductService {

    private final ICreateProductUc createProductUc;
    private final IGetAllProductsUc getAllProductsUc;
    private final ISearchProductByNameUc searchProductByNameUc;
    private final IUpdateProductUc updateProductUc;
    private final IDeleteProductUc deleteProductUc;
    private final IGetProductUc getProductUc;
    private final IGetProductsByCategoryIdUc getProductsByCategoryIdUc;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        return createProductUc.createProduct(request);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return getAllProductsUc.getAllProducts();
    }

    @Override
    public ProductCursorPageResponse getAllProductsByCursor(String cursor, Integer limit) {
        return getAllProductsUc.getAllProductsByCursor(cursor, limit);
    }

    @Override
    public List<ProductEntity> searchProductByName(String name) {
        return searchProductByNameUc.searchProductByName(name);
    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest request) {
        return updateProductUc.updateProduct(request);
    }

    @Override
    public void deleteProduct(String id) {
        deleteProductUc.deleteProduct(id);
    }

    @Override
    public ProductEntity getProductByIdOrSlug(String idOrSlug) {
        return getProductUc.getProductByIdOrSlug(idOrSlug);
    }

    @Override
    public List<ProductEntity> getProductsByCategoryId(String categoryId) {
        return getProductsByCategoryIdUc.getProductsByCategoryId(categoryId);
    }
}
