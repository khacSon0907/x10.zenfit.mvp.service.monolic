package x10.zenfit.mvp.service.service.cartService.addItemToCartUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemReq {
    private String productId;
    private String colorId;
    private String sizeId;
    private Integer quantity;
    private BigDecimal unitPrice; // optional - service may override
}

