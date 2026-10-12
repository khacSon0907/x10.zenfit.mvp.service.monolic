package x10.zenfit.mvp.service.service.productService.searchProductByNameUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchProductByNameUcImpl implements ISearchProductByNameUc {

    private final IProductRepository productRepository;

    @Override
    public List<ProductEntity> searchProductByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return productRepository.findByNameContainingIgnoreCase(name.trim());
    }
}
