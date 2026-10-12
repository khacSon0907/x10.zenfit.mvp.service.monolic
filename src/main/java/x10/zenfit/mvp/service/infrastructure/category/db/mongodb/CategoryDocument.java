package x10.zenfit.mvp.service.infrastructure.category.db.mongodb;

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
@Document(collection = "categories")
public class CategoryDocument {
    @Id
    private String id;
    private String name;
    private String slug;
    private String description;
    private boolean active;
    private String bannerUrl;
    private String parentId;
    private Integer sortOrder;
    private Instant createdAt;
    private Instant updatedAt;
}

