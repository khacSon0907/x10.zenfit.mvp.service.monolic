package x10.zenfit.mvp.service.service.cartService.updateCartItemUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemUcReq {
    private String colorId;
    private String colorName;

    private String sizeId;
    private String sizeName;

    private Integer quantity;
}

