package x10.zenfit.mvp.service.infrastructure.product.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "products")
public class ProductDocument {

    @Id
    private String id;

    private String name;

    private String slug;

    private String categoryId;

    private String description;

    private BigDecimal price;

    private BigDecimal salePrice;

    private Boolean active;

    private List<ColorVariantDocument> colors;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
