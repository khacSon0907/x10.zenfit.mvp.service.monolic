package x10.zenfit.mvp.service.service.orderService;

import lombok.Builder;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminOrderSocketMessage {

    private String type;
    private String orderId;
    private String orderCode;
    private String userId;
    private String guestId;
    private String guestEmail;
    private String receiverName;
    private String receiverPhone;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private OrderStatus status;
    private Integer itemCount;
    private LocalDateTime createdAt;
    private String message;
}
