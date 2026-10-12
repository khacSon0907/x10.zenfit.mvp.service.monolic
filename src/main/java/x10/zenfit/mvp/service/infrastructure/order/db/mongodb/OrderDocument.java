package x10.zenfit.mvp.service.infrastructure.order.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class OrderDocument {

    @Id
    private String id;
    private String orderCode;
    private Long payosOrderCode;
    private String userId;
    private String guestId;
    private String guestEmail;
    private String guestUsername;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private List<OrderItemDocument> items;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String voucherId;
    private String voucherCode;
    private Long pointsEarned;
    private Boolean pointsAwarded;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private OrderStatus status;
    private String note;
    private String cancelReason;
    private CancelReasonType cancelReasonType;
    private String cancelReasonDetail;
    private String trackingCode;
    private String shippingProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
