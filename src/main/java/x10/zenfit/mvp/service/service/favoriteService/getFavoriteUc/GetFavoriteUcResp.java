package x10.zenfit.mvp.service.service.favoriteService.getFavoriteUc;

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
public class GetFavoriteUcResp {
    private String favoriteId;
    private String userId;
    private List<FavoriteItemResp> items;
    private Integer totalItems;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteItemResp {
        private String productId;
        private String productName;
        private String productImage;
        private BigDecimal price;
    }
}

