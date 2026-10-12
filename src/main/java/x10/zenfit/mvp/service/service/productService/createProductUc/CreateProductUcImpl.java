package x10.zenfit.mvp.service.service.productService.createProductUc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductImageEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CreateProductUcImpl implements ICreateProductUc {

    private final IProductRepository productRepository;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        String slug = generateUniqueSlug(request.getSlug(), request.getName());

        List<ColorVariantEntity> colors = Collections.emptyList();
        if (request.getColors() != null) {
            colors = request.getColors().stream().map(colorDto -> {
                List<ProductImageEntity> images = Collections.emptyList();
                if (colorDto.getImages() != null) {
                    images = colorDto.getImages().stream().map(imgDto ->
                            ProductImageEntity.builder()
                                    .id(UUID.randomUUID().toString())
                                    .url(imgDto.getUrl())
                                    .publicId(imgDto.getPublicId())
                                    .main(imgDto.getMain() != null ? imgDto.getMain() : false)
                                    .sortOrder(imgDto.getSortOrder())
                                    .build()
                    ).collect(Collectors.toList());
                }

                List<SizeVariantEntity> sizes = Collections.emptyList();
                if (colorDto.getSizes() != null) {
                    sizes = colorDto.getSizes().stream().map(sizeDto ->
                            SizeVariantEntity.builder()
                                    .id(UUID.randomUUID().toString())
                                    .size(sizeDto.getSize())
                                    .sku(resolveSku(sizeDto.getSku(), slug, colorDto.getColorName(), sizeDto.getSize()))
                                    .quantity(sizeDto.getQuantity() != null ? sizeDto.getQuantity() : 0)
                                    .soldQuantity(sizeDto.getSoldQuantity() != null ? sizeDto.getSoldQuantity() : 0)
                                    .build()
                    ).collect(Collectors.toList());
                }

                return ColorVariantEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .colorName(colorDto.getColorName())
                        .colorCode(colorDto.getColorCode())
                        .images(images)
                        .sizes(sizes)
                        .build();
            }).collect(Collectors.toList());
        }

        ProductEntity product = ProductEntity.builder()
                .name(request.getName())
                .slug(slug)
                .categoryId(request.getCategoryId())
                .description(request.getDescription())
                .price(request.getPrice())
                .salePrice(request.getSalePrice())
                .active(request.getActive() != null ? request.getActive() : true)
                .colors(colors)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductEntity savedProduct = productRepository.save(product);

        return CreateProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .slug(savedProduct.getSlug())
                .categoryId(savedProduct.getCategoryId())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .salePrice(savedProduct.getSalePrice())
                .active(savedProduct.getActive())
                .colors(savedProduct.getColors())
                .createdAt(savedProduct.getCreatedAt())
                .updatedAt(savedProduct.getUpdatedAt())
                .build();
    }

    private String generateSlug(String input) {
        if (input == null) return "san-pham";
        String normalized = Normalizer.normalize(input.replace('Đ', 'D').replace('đ', 'd'), Normalizer.Form.NFD);
        String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String slug = noDiacritics.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        return slug.isBlank() ? "san-pham" : slug;
    }

    private String generateUniqueSlug(String requestedSlug, String productName) {
        String baseSlug = requestedSlug != null && !requestedSlug.isBlank()
                ? generateSlug(requestedSlug)
                : generateSlug(productName);

        String uniqueSlug = baseSlug;
        int suffix = 2;

        while (productRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + suffix;
            suffix++;
        }

        return uniqueSlug;
    }

    private String resolveSku(String requestedSku, String productSlug, String colorName, String size) {
        if (requestedSku != null && !requestedSku.isBlank()) {
            return requestedSku.trim();
        }

        return generateSlug(productSlug + "-" + colorName + "-" + size).toUpperCase();
    }
}
