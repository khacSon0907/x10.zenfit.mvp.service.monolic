package x10.zenfit.mvp.service.infrastructure.review.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class ReviewDocument {

    @Id
    private String id;

    private String productId;

    private String userId;

    private Integer rating;

    private String comment;

    private List<String> images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
