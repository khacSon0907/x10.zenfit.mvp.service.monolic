package x10.zenfit.mvp.service.infrastructure.cart.adapter;

import x10.zenfit.mvp.service.common.domain.entities.cart.CartEntity;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartItem;
import x10.zenfit.mvp.service.infrastructure.cart.db.mongodb.CartDocument;
import x10.zenfit.mvp.service.infrastructure.cart.db.mongodb.CartItemDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartEntity toEntity(CartDocument doc) {
        if (doc == null) {
            return null;
        }

        List<CartItem> items = doc.getItems() == null
                ? new ArrayList<>()
                : doc.getItems().stream()
                .map(CartMapper::itemDocToEntity)
                .collect(Collectors.toList());

        return CartEntity.builder()
                .cartId(doc.getId())
                .userId(doc.getUserId())
                .items(items)
                .totalQuantity(doc.getTotalQuantity())
                .totalAmount(doc.getTotalAmount())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static CartDocument toDocument(CartEntity entity) {
        if (entity == null) {
            return null;
        }

        List<CartItemDocument> items = entity.getItems() == null
                ? new ArrayList<>()
                : entity.getItems().stream()
                .map(CartMapper::itemEntityToDoc)
                .collect(Collectors.toList());

        return CartDocument.builder()
                .id(entity.getCartId())
                .userId(entity.getUserId())
                .items(items)
                .totalQuantity(entity.getTotalQuantity())
                .totalAmount(entity.getTotalAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private static CartItem itemDocToEntity(CartItemDocument doc) {
        if (doc == null) {
            return null;
        }

        return CartItem.builder()
                .productId(doc.getProductId())
                .productName(doc.getProductName())
                .productImage(doc.getProductImage())
                .colorId(doc.getColorId())
                .colorName(doc.getColorName())
                .sizeId(doc.getSizeId())
                .sizeName(doc.getSizeName())
                .quantity(doc.getQuantity())
                .unitPrice(doc.getUnitPrice())
                .build();
    }

    private static CartItemDocument itemEntityToDoc(CartItem entity) {
        if (entity == null) {
            return null;
        }

        return CartItemDocument.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .productImage(entity.getProductImage())
                .colorId(entity.getColorId())
                .colorName(entity.getColorName())
                .sizeId(entity.getSizeId())
                .sizeName(entity.getSizeName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }
}