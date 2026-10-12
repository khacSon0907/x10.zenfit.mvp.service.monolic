package x10.zenfit.mvp.service.service.orderService.updateOrderUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateOrderRequest {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    @Valid
    private List<OrderItemRequest> items;

    @DecimalMin(value = "0.0", message = "Phi van chuyen khong duoc am")
    private BigDecimal shippingFee;

    @DecimalMin(value = "0.0", message = "So tien giam gia khong duoc am")
    private BigDecimal discountAmount;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private OrderStatus status;
    private String note;
    private String cancelReason;
    private CancelReasonType cancelReasonType;
    private String cancelReasonDetail;
    private String trackingCode;
    private String shippingProvider;

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
