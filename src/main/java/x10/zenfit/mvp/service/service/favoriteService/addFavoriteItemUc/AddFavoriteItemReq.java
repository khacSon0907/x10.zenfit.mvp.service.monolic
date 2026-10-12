package x10.zenfit.mvp.service.service.favoriteService.addFavoriteItemUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddFavoriteItemReq {

    @NotBlank
    private String productId;
}

