package x10.zenfit.mvp.service.service.cartService.removeItemFromCartUc;

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

@Service
@RequiredArgsConstructor
public class RemoveItemFromCartUcImpl implements IRemoveItemFromCartUc {

    private final ICartRepository cartRepository;

    @Override
    public String execute(String userId, String productId) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(CartError.CART_NOT_FOUND));

        List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : new ArrayList<>(cart.getItems());
        boolean removed = items.removeIf(i -> productId.equals(i.getProductId()));

        if (!removed) {
            throw new BusinessException(CartError.CART_ITEM_NOT_FOUND);
        }

        cart.setItems(items);
        recalculateCart(cart);
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);

        return "Item removed successfully";
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
}
