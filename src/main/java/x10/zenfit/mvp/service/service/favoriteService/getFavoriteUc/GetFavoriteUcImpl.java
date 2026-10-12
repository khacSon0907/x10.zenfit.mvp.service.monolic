package x10.zenfit.mvp.service.service.favoriteService.getFavoriteUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IFavoriteRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteEntity;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductImageEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFavoriteUcImpl implements IGetFavoriteUc {

    private final IFavoriteRepository favoriteRepository;
    private final IProductRepository productRepository;

    @Override
    public GetFavoriteUcResp execute(String userId) {
        FavoriteEntity favorite = favoriteRepository.findByUserId(userId)
                .orElseGet(() -> {
                    FavoriteEntity newFav = FavoriteEntity.builder()
                            .favoriteId(UUID.randomUUID().toString())
                            .userId(userId)
                            .items(new ArrayList<>())
                            .totalItems(0)
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();
                    return favoriteRepository.save(newFav);
                });

        return mapToResp(favorite);
    }

    private GetFavoriteUcResp mapToResp(FavoriteEntity favorite) {
        List<GetFavoriteUcResp.FavoriteItemResp> items = favorite.getItems() == null ? new ArrayList<>() :
                favorite.getItems().stream()
                        .map(item -> GetFavoriteUcResp.FavoriteItemResp.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .productImage(resolveFavoriteImage(item))
                                .price(resolveFavoritePrice(item))
                                .build())
                        .collect(Collectors.toList());

        return GetFavoriteUcResp.builder()
                .favoriteId(favorite.getFavoriteId())
                .userId(favorite.getUserId())
                .items(items)
                .totalItems(favorite.getTotalItems())
                .createdAt(favorite.getCreatedAt())
                .updatedAt(favorite.getUpdatedAt())
                .build();
    }

    private String resolveFavoriteImage(FavoriteItem item) {
        if (item == null) {
            return null;
        }

        if (item.getProductImage() != null && !item.getProductImage().isBlank()) {
            return item.getProductImage();
        }

        return productRepository.findById(item.getProductId())
                .map(this::resolveProductImage)
                .orElse(null);
    }

    private BigDecimal resolveFavoritePrice(FavoriteItem item) {
        if (item == null) {
            return null;
        }

        if (item.getPrice() != null && item.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            return item.getPrice();
        }

        return productRepository.findById(item.getProductId())
                .map(ProductEntity::getPrice)
                .orElse(item.getPrice());
    }

    private String resolveProductImage(ProductEntity product) {
        if (product == null || product.getColors() == null) {
            return null;
        }

        return product.getColors().stream()
                .filter(color -> color != null && color.getImages() != null)
                .flatMap(color -> color.getImages().stream())
                .filter(image -> image != null && image.getUrl() != null && !image.getUrl().isBlank())
                .filter(image -> Boolean.TRUE.equals(image.getMain()))
                .map(ProductImageEntity::getUrl)
                .findFirst()
                .orElseGet(() -> product.getColors().stream()
                        .filter(color -> color != null && color.getImages() != null)
                        .flatMap(color -> color.getImages().stream())
                        .filter(image -> image != null && image.getUrl() != null && !image.getUrl().isBlank())
                        .map(ProductImageEntity::getUrl)
                        .findFirst()
                        .orElse(null));
    }
}

