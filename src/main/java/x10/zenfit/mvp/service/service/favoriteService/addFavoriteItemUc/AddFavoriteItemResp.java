package x10.zenfit.mvp.service.service.favoriteService.addFavoriteItemUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddFavoriteItemResp {
    private String favoriteId;
    private String userId;
    private String productId;
}

