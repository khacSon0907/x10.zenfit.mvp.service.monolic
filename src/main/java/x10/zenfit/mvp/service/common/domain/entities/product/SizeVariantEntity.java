package x10.zenfit.mvp.service.common.domain.entities.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeVariantEntity {

    private String id;

    private String size; // S, M, L, XL

    private String sku; // VD: TSHIRT-BLACK-M

    private Integer quantity;

    private Integer soldQuantity;
}
