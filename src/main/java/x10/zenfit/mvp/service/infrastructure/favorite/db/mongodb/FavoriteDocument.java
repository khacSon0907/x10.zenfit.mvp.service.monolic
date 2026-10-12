package x10.zenfit.mvp.service.infrastructure.favorite.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "favorites")
public class FavoriteDocument {
    @Id
    private String id;

    private String userId;

    private List<FavoriteItemDocument> items;

    private Integer totalItems;

    private Instant createdAt;

    private Instant updatedAt;
}

