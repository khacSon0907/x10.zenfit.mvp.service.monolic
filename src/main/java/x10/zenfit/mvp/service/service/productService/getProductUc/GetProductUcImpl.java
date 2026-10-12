package x10.zenfit.mvp.service.service.productService.getProductUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetProductUcImpl implements IGetProductUc {

    private final IProductRepository productRepository;

    @Override
    public ProductEntity getProductByIdOrSlug(String idOrSlug) {
        if (idOrSlug == null || idOrSlug.isBlank()) {
            throw new IllegalArgumentException("ID hoặc Slug là bắt buộc");
        }

        Optional<ProductEntity> productOpt = productRepository.findById(idOrSlug);
        
        if (productOpt.isEmpty()) {
            productOpt = productRepository.findBySlug(idOrSlug);
        }

        return productOpt.orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));
    }
}
