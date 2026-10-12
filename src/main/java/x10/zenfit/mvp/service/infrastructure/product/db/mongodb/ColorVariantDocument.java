package x10.zenfit.mvp.service.infrastructure.product.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorVariantDocument {
    private String id;

    private String colorName; // Black, White, Red

    private String colorCode; // #000000, #FFFFFF

    private List<ProductImageDocument> images;

    private List<SizeVariantDocument> sizes;
}
