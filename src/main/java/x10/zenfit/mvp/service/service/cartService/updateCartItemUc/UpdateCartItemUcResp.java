package x10.zenfit.mvp.service.service.cartService.updateCartItemUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemUcResp {
    private String cartId;
    private String userId;
    private List<CartItemResp> items;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemResp {
        private String productId;
        private String productName;
        private String productImage;

        private String colorId;
        private String colorName;

        private String sizeId;
        private String sizeName;

        private Integer quantity;
        private BigDecimal unitPrice;
    }
}

