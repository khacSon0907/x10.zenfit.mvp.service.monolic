package x10.zenfit.mvp.service.service.cartService.updateCartItemUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemReq {
    private String productId;
    private String colorId;
    private String sizeId;
    private Integer quantity;
}

