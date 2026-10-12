package x10.zenfit.mvp.service.service.favoriteService.addFavoriteItemUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IFavoriteRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteEntity;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductImageEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddFavoriteItemUcImpl implements IAddFavoriteItemUc {

    private final IFavoriteRepository favoriteRepository;
    private final IProductRepository productRepository;

    @Override
    public AddFavoriteItemResp execute(String userId, AddFavoriteItemReq req) {
        Optional<ProductEntity> productOpt = productRepository.findById(req.getProductId());
        if (productOpt.isEmpty()) {
            throw new BusinessException(ProductError.PRODUCT_NOT_FOUND);
        }

        ProductEntity product = productOpt.get();

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

        // check if already exists
        boolean exists = favorite.getItems().stream()
                .anyMatch(i -> i.getProductId().equals(req.getProductId()));

        if (!exists) {
            FavoriteItem item = FavoriteItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(resolveProductImage(product))
                    .price(product.getPrice())
                    .build();
            favorite.getItems().add(item);
            favorite.setTotalItems(favorite.getItems().size());
            favorite.setUpdatedAt(Instant.now());
            favoriteRepository.save(favorite);
        }

        return AddFavoriteItemResp.builder()
                .favoriteId(favorite.getFavoriteId())
                .userId(userId)
                .productId(req.getProductId())
                .build();
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
