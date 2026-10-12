package x10.zenfit.mvp.service.common.domain.entities.banner;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "banners")
public class BannerEntity {

    @Id
    private String id;

    private String title;        // Ví dụ: Mừng đại lễ
    private String subtitle;     // Ví dụ: Lên đồ đi biển
    private String imageUrl;     // URL Cloudinary
    private String linkUrl;      // Ví dụ: /collections/sale-2026

    private Boolean active;
    private Integer sortOrder;

    private Instant createdAt;
    private Instant updatedAt;
}