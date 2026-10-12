package x10.zenfit.mvp.service.infrastructure.cart.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDocument {
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

