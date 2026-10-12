package x10.zenfit.mvp.service.common.domain.entities.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageEntity {

    private String id;

    private String url;

    private String publicId; // Cloudinary public_id

    private Boolean main; // ảnh chính hay không

    private Integer sortOrder; // thứ tự hiển thị ảnh
}