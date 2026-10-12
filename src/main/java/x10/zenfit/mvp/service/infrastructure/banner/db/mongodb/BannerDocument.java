package x10.zenfit.mvp.service.infrastructure.banner.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "banners")
public class BannerDocument {
    @Id
    private String id;
    private String title;
    private String subtitle;
    private String imageUrl;
    private String linkUrl;
    private Boolean active;
    private Integer sortOrder;
    private Instant createdAt;
    private Instant updatedAt;
}
