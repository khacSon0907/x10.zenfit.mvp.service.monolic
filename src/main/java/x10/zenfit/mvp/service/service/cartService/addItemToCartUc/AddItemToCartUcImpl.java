package x10.zenfit.mvp.service.service.cartService.addItemToCartUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.ICartRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartEntity;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddItemToCartUcImpl implements IAddItemToCartUc {

    private final ICartRepository cartRepository;
    private final IProductRepository productRepository;

    @Override
    public AddItemToCartUcResp execute(String userId, AddItemToCartUcReq req) {
        // Validate product exists
        Optional<ProductEntity> productOpt = productRepository.findById(req.getProductId());
        if (productOpt.isEmpty()) {
            throw new BusinessException(ProductError.PRODUCT_NOT_FOUND);
        }

        ProductEntity product = productOpt.get();
        BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();

        // Get cart or create new
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.builder()
                            .cartId(java.util.UUID.randomUUID().toString())
                            .userId(userId)
                            .items(new ArrayList<>())
                            .totalAmount(BigDecimal.ZERO)
                            .totalQuantity(0)
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        // Find existing item or create new
        List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : new ArrayList<>(cart.getItems());
        Optional<CartItem> existingItem = findCartItem(items, req);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(req.getProductId())
                    .productName(req.getProductName())
                    .productImage(req.getProductImage())
                    .colorId(req.getColorId())
                    .colorName(req.getColorName())
                    .sizeId(req.getSizeId())
                    .sizeName(req.getSizeName())
                    .quantity(req.getQuantity())
                    .unitPrice(unitPrice)
                    .build();
            items.add(newItem);
        }

        cart.setItems(items);
        recalculateCart(cart);
        cart.setUpdatedAt(Instant.now());
        CartEntity savedCart = cartRepository.save(cart);

        return mapToResp(savedCart);
    }

    private Optional<CartItem> findCartItem(List<CartItem> items, AddItemToCartUcReq req) {
        return items.stream()
                .filter(i -> req.getProductId().equals(i.getProductId())
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

    private AddItemToCartUcResp mapToResp(CartEntity cart) {
        List<AddItemToCartUcResp.CartItemResp> items = cart.getItems() == null ? new ArrayList<>() :
                cart.getItems().stream()
                        .map(item -> AddItemToCartUcResp.CartItemResp.builder()
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

        return AddItemToCartUcResp.builder()
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
