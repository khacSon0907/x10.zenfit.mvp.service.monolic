package x10.zenfit.mvp.service.service.orderService.createOrderUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {

    private String userId;

    @Valid
    private GuestRequest guest;

    @NotBlank(message = "Ten nguoi nhan khong duoc de trong")
    private String receiverName;

    @NotBlank(message = "So dien thoai nguoi nhan khong duoc de trong")
    private String receiverPhone;

    @NotBlank(message = "Dia chi nguoi nhan khong duoc de trong")
    private String receiverAddress;

    @Valid
    @NotEmpty(message = "Don hang phai co san pham")
    private List<OrderItemRequest> items;


    private BigDecimal shippingFee ;

    @DecimalMin(value = "0.0", message = "So tien giam gia khong duoc am")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    private String voucherCode;

    private PaymentMethod paymentMethod = PaymentMethod.COD;

    private String note;

    @AssertTrue(message = "Can co userId hoac thong tin guest")
    public boolean isBuyerValid() {
        boolean hasUser = userId != null && !userId.isBlank();
        boolean hasGuest = guest != null
                && guest.getEmail() != null
                && !guest.getEmail().isBlank()
                && guest.getUsername() != null
                && !guest.getUsername().isBlank();

        return hasUser || hasGuest;
    }

    @Data
    public static class GuestRequest {
        @NotBlank(message = "Email guest khong duoc de trong")
        @Email(message = "Email guest khong dung dinh dang")
        private String email;

        @NotBlank(message = "Ten guest khong duoc de trong")
        private String username;
    }

    @Data
    public static class OrderItemRequest {
        @NotBlank(message = "San pham khong duoc de trong")
        private String productId;
        private String productName;
        private String productImage;
        private String colorId;
        private String colorName;
        private String sizeId;
        private String sizeName;

        @NotNull(message = "So luong khong duoc de trong")
        @Min(value = 1, message = "So luong phai lon hon 0")
        private Integer quantity;
    }
}
