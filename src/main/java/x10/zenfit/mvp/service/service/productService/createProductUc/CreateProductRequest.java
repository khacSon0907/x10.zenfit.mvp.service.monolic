package x10.zenfit.mvp.service.service.productService.createProductUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    private String slug;

    @NotBlank(message = "Danh mục không được để trống")
    private String categoryId;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá không được âm")
    private BigDecimal price;

    @Min(value = 0, message = "Giá khuyến mãi không được âm")
    private BigDecimal salePrice;

    private Boolean active = true;

    @Valid
    private List<ColorVariantDto> colors;

    @Data
    public static class ColorVariantDto {
        @NotBlank(message = "Tên màu không được để trống")
        private String colorName;
        private String colorCode;

        @Valid
        private List<ProductImageDto> images;

        @Valid
        private List<SizeVariantDto> sizes;
    }

    @Data
    public static class ProductImageDto {
        @NotBlank(message = "URL ảnh không được để trống")
        private String url;
        private String publicId;
        private Boolean main = false;
        private Integer sortOrder;
    }

    @Data
    public static class SizeVariantDto {
        @NotBlank(message = "Kích thước không được để trống")
        private String size;
        private String sku;
        
        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 0, message = "Số lượng không được âm")
        private Integer quantity;

        @Min(value = 0, message = "So luong da ban khong duoc am")
        private Integer soldQuantity;
    }
}
