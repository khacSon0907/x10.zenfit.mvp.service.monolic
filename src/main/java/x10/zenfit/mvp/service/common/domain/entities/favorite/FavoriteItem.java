package x10.zenfit.mvp.service.common.domain.entities.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItem {

    private String productId;

    private String productName;

    private String productImage;

    private BigDecimal price;
}