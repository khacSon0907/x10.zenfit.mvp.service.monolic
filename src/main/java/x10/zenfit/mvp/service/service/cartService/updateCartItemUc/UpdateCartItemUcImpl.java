package x10.zenfit.mvp.service.service.cartService.updateCartItemUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.ICartRepository;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartEntity;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartItem;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.cart.CartError;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateCartItemUcImpl implements IUpdateCartItemUc {

    private final ICartRepository cartRepository;

    @Override
    public UpdateCartItemUcResp execute(String userId, String productId, UpdateCartItemUcReq req) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(CartError.CART_NOT_FOUND));

        List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : new ArrayList<>(cart.getItems());
        Optional<CartItem> existingItem = findCartItem(items, productId, req);

        if (existingItem.isEmpty()) {
            throw new BusinessException(CartError.CART_ITEM_NOT_FOUND);
        }

        CartItem item = existingItem.get();
        item.setQuantity(req.getQuantity());

        cart.setItems(items);
        recalculateCart(cart);
        cart.setUpdatedAt(Instant.now());
        CartEntity savedCart = cartRepository.save(cart);

        return mapToResp(savedCart);
    }

    private Optional<CartItem> findCartItem(List<CartItem> items, String productId, UpdateCartItemUcReq req) {
        return items.stream()
                .filter(i -> productId.equals(i.getProductId())
                        && compareString(req.getColorId(), i.getColorId())
                        && compareString(req.getSizeId(), i.getSizeId()))
                .findFirst();
    }

    private boolean compareString(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    private void recalculateCart(CartEntity cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            cart.setTotalQuantity(0);
            cart.setTotalAmount(BigDecimal.ZERO);
            return;
        }

        int totalQty = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalAmount = cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalQuantity(totalQty);
        cart.setTotalAmount(totalAmount);
    }

    private UpdateCartItemUcResp mapToResp(CartEntity cart) {
        List<UpdateCartItemUcResp.CartItemResp> items = cart.getItems() == null ? new ArrayList<>() :
                cart.getItems().stream()
                        .map(item -> UpdateCartItemUcResp.CartItemResp.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .productImage(item.getProductImage())
                                .colorId(item.getColorId())
                                .colorName(item.getColorName())
                                .sizeId(item.getSizeId())
                                .sizeName(item.getSizeName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .build())
                        .collect(Collectors.toList());

        return UpdateCartItemUcResp.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .items(items)
                .totalQuantity(cart.getTotalQuantity())
                .totalAmount(cart.getTotalAmount())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
