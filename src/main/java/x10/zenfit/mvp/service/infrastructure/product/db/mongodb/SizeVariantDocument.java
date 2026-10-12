package x10.zenfit.mvp.service.infrastructure.product.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeVariantDocument {
    private String id;

    private String size; // S, M, L, XL

    private String sku; // VD: TSHIRT-BLACK-M

    private Integer quantity;

    private Integer soldQuantity;
}
