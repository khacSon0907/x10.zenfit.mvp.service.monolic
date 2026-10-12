package x10.zenfit.mvp.service.infrastructure.favorite.adapter;

import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteEntity;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteItem;
import x10.zenfit.mvp.service.infrastructure.favorite.db.mongodb.FavoriteDocument;
import x10.zenfit.mvp.service.infrastructure.favorite.db.mongodb.FavoriteItemDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteMapper {

    public static FavoriteEntity toEntity(FavoriteDocument doc) {
        if (doc == null) {
            return null;
        }

        List<FavoriteItem> items = doc.getItems() == null
                ? new ArrayList<>()
                : doc.getItems().stream()
                .map(FavoriteMapper::itemDocToEntity)
                .collect(Collectors.toList());

        return FavoriteEntity.builder()
                .favoriteId(doc.getId())
                .userId(doc.getUserId())
                .items(items)
                .totalItems(doc.getTotalItems())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static FavoriteDocument toDocument(FavoriteEntity entity) {
        if (entity == null) {
            return null;
        }

        List<FavoriteItemDocument> items = entity.getItems() == null
                ? new ArrayList<>()
                : entity.getItems().stream()
                .map(FavoriteMapper::itemEntityToDoc)
                .collect(Collectors.toList());

        return FavoriteDocument.builder()
                .id(entity.getFavoriteId())
                .userId(entity.getUserId())
                .items(items)
                .totalItems(entity.getTotalItems())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private static FavoriteItem itemDocToEntity(FavoriteItemDocument doc) {
        if (doc == null) {
            return null;
        }

        return FavoriteItem.builder()
                .productId(doc.getProductId())
                .productName(doc.getProductName())
                .productImage(doc.getProductImage())
                .price(doc.getUnitPrice())
                .build();
    }

    private static FavoriteItemDocument itemEntityToDoc(FavoriteItem entity) {
        if (entity == null) {
            return null;
        }

        return FavoriteItemDocument.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .productImage(entity.getProductImage())
                .unitPrice(entity.getPrice())
                .quantity(null)
                .build();
    }
}

