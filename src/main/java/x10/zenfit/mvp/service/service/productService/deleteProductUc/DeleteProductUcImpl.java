package x10.zenfit.mvp.service.service.productService.deleteProductUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

@Component
@RequiredArgsConstructor
public class DeleteProductUcImpl implements IDeleteProductUc {

    private final IProductRepository productRepository;

    @Override
    public void deleteProduct(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID là bắt buộc");
        }
        productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));
        productRepository.deleteById(id);
    }
}
