package x10.zenfit.mvp.service.common.domain.entities.cart;

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
public class CartEntity {

    private String cartId;

    private String userId;

    private List<CartItem> items;

    private Integer totalQuantity;

    private BigDecimal totalAmount;

    private Instant createdAt;

    private Instant updatedAt;
}