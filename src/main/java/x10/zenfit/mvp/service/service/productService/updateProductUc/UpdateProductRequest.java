package x10.zenfit.mvp.service.service.productService.updateProductUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {

    private String id;

    @Size(max = 200, message = "Tên sản phẩm không được vượt quá 200 ký tự")
    private String name;

    private String slug;

    private String categoryId;

    private String description;

    @Min(value = 0, message = "Giá không được âm")
    private BigDecimal price;

    @Min(value = 0, message = "Giá khuyến mãi không được âm")
    private BigDecimal salePrice;

    private Boolean active;

    @Valid
    private List<ColorVariantDto> colors;

    @Data
    public static class ColorVariantDto {
        private String id;
        private String colorName;
        private String colorCode;

        @Valid
        private List<ProductImageDto> images;

        @Valid
        private List<SizeVariantDto> sizes;
    }

    @Data
    public static class ProductImageDto {
        private String id;
        private String url;
        private String publicId;
        private Boolean main;
        private Integer sortOrder;
    }

    @Data
    public static class SizeVariantDto {
        private String id;
        private String size;
        private String sku;

        @Min(value = 0, message = "Số lượng không được âm")
        private Integer quantity;

        @Min(value = 0, message = "So luong da ban khong duoc am")
        private Integer soldQuantity;
    }
}
