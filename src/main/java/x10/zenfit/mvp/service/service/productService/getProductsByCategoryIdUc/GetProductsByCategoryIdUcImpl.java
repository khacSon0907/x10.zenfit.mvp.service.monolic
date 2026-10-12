package x10.zenfit.mvp.service.service.productService.getProductsByCategoryIdUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetProductsByCategoryIdUcImpl implements IGetProductsByCategoryIdUc {

    private final IProductRepository productRepository;

    @Override
    public List<ProductEntity> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}

