package x10.zenfit.mvp.service.service.productService.updateProductUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductImageEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateProductUcImpl implements IUpdateProductUc {

    private final IProductRepository productRepository;

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest request) {
        ProductEntity existing = productRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));

        if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName().trim());
        }

        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            existing.setSlug(generateUniqueSlug(request.getSlug(), existing.getId()));
        } else if (request.getName() != null && !request.getName().isBlank()) {
            existing.setSlug(generateUniqueSlug(request.getName(), existing.getId()));
        }

        if (request.getCategoryId() != null && !request.getCategoryId().isBlank()) {
            existing.setCategoryId(request.getCategoryId());
        }

        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }

        if (request.getSalePrice() != null) {
            existing.setSalePrice(request.getSalePrice());
        }

        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }

        if (request.getColors() != null) {
            List<ColorVariantEntity> colors = request.getColors().stream().map(colorDto -> {
                List<ProductImageEntity> images = Collections.emptyList();
                if (colorDto.getImages() != null) {
                    images = colorDto.getImages().stream().map(imgDto ->
                            ProductImageEntity.builder()
                                    .id(imgDto.getId() != null && !imgDto.getId().isBlank() ? imgDto.getId() : UUID.randomUUID().toString())
                                    .url(imgDto.getUrl())
                                    .publicId(imgDto.getPublicId())
                                    .main(imgDto.getMain() != null ? imgDto.getMain() : false)
                                    .sortOrder(imgDto.getSortOrder())
                                    .build()
                    ).collect(Collectors.toList());
                }

                List<SizeVariantEntity> sizes = Collections.emptyList();
                if (colorDto.getSizes() != null) {
                    sizes = colorDto.getSizes().stream().map(sizeDto -> {
                        SizeVariantEntity existingSize = findExistingSize(existing, sizeDto.getId());

                        return SizeVariantEntity.builder()
                                .id(sizeDto.getId() != null && !sizeDto.getId().isBlank() ? sizeDto.getId() : UUID.randomUUID().toString())
                                .size(sizeDto.getSize())
                                .sku(resolveSku(sizeDto.getSku(), existing.getSlug(), colorDto.getColorName(), sizeDto.getSize()))
                                .quantity(sizeDto.getQuantity() != null ? sizeDto.getQuantity() : 0)
                                .soldQuantity(resolveSoldQuantity(sizeDto.getSoldQuantity(), existingSize))
                                .build();
                    }).collect(Collectors.toList());
                }

                return ColorVariantEntity.builder()
                        .id(colorDto.getId() != null && !colorDto.getId().isBlank() ? colorDto.getId() : UUID.randomUUID().toString())
                        .colorName(colorDto.getColorName())
                        .colorCode(colorDto.getColorCode())
                        .images(images)
                        .sizes(sizes)
                        .build();
            }).collect(Collectors.toList());

            existing.setColors(colors);
        }

        existing.setUpdatedAt(LocalDateTime.now());

        ProductEntity saved = productRepository.save(existing);

        return UpdateProductResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .slug(saved.getSlug())
                .categoryId(saved.getCategoryId())
                .description(saved.getDescription())
                .price(saved.getPrice())
                .salePrice(saved.getSalePrice())
                .active(saved.getActive())
                .colors(saved.getColors())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    private String generateSlug(String input) {
        if (input == null) return "san-pham";
        String normalized = Normalizer.normalize(input.replace('Đ', 'D').replace('đ', 'd'), Normalizer.Form.NFD);
        String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String slug = noDiacritics.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        return slug.isBlank() ? "san-pham" : slug;
    }

    private String generateUniqueSlug(String input, String currentProductId) {
        String baseSlug = generateSlug(input);
        String uniqueSlug = baseSlug;
        int suffix = 2;

        while (true) {
            Optional<ProductEntity> withSlug = productRepository.findBySlug(uniqueSlug);

            if (withSlug.isEmpty() || withSlug.get().getId().equals(currentProductId)) {
                return uniqueSlug;
            }

            uniqueSlug = baseSlug + "-" + suffix;
            suffix++;
        }
    }

    private String resolveSku(String requestedSku, String productSlug, String colorName, String size) {
        if (requestedSku != null && !requestedSku.isBlank()) {
            return requestedSku.trim();
        }

        return generateSlug(productSlug + "-" + colorName + "-" + size).toUpperCase();
    }

    private SizeVariantEntity findExistingSize(ProductEntity product, String sizeId) {
        if (product.getColors() == null || sizeId == null || sizeId.isBlank()) {
            return null;
        }

        return product.getColors().stream()
                .filter(color -> color.getSizes() != null)
                .flatMap(color -> color.getSizes().stream())
                .filter(size -> sizeId.equals(size.getId()))
                .findFirst()
                .orElse(null);
    }

    private Integer resolveSoldQuantity(Integer requestedSoldQuantity, SizeVariantEntity existingSize) {
        if (requestedSoldQuantity != null) {
            return requestedSoldQuantity;
        }

        if (existingSize != null && existingSize.getSoldQuantity() != null) {
            return existingSize.getSoldQuantity();
        }

        return 0;
    }
}
