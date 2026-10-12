package x10.zenfit.mvp.service.service.cartService.getCartUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.ICartRepository;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCartUcImpl implements IGetCartUc {

    private final ICartRepository cartRepository;

    @Override
    public GetCartUcResp execute(String userId) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.builder()
                            .cartId(UUID.randomUUID().toString())
                            .userId(userId)
                            .items(new ArrayList<>())
                            .totalAmount(BigDecimal.ZERO)
                            .totalQuantity(0)
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        return mapToResp(cart);
    }

    private GetCartUcResp mapToResp(CartEntity cart) {
        List<GetCartUcResp.CartItemResp> items = cart.getItems() == null ? new ArrayList<>() :
                cart.getItems().stream()
                        .map(item -> GetCartUcResp.CartItemResp.builder()
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

        return GetCartUcResp.builder()
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

