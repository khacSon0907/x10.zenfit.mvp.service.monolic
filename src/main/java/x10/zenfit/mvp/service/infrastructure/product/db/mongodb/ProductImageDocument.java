package x10.zenfit.mvp.service.infrastructure.product.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDocument {
    private String id;

    private String url;

    private String publicId; // Cloudinary public_id

    private Boolean main; // ảnh chính hay không

    private Integer sortOrder; // thứ tự hiển thị ảnh
}
