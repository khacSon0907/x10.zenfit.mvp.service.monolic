package x10.zenfit.mvp.service.common.domain.entities.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorVariantEntity {
    private String id;

    private String colorName; // Black, White, Red

    private String colorCode; // #000000, #FFFFFF

    private List<ProductImageEntity> images;

    private List<SizeVariantEntity> sizes ;
}