package x10.zenfit.mvp.service.common.domain.entities.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteEntity {

    private String favoriteId;

    private String userId;

    private List<FavoriteItem> items;

    private Integer totalItems;

    private Instant createdAt;

    private Instant updatedAt;
}