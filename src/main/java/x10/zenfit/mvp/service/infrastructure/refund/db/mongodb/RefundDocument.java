package x10.zenfit.mvp.service.infrastructure.refund.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.RefundStatus;
import x10.zenfit.mvp.service.common.domain.enums.RefundType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refunds")
public class RefundDocument {

    @Id
    private String id;
    private String orderId;
    private String paymentId;
    private String userId;
    private RefundType type;
    private BigDecimal refundAmount;
    private String reason;
    private List<String> imageUrls;
    private String productId;
    private String productName;
    private String colorId;
    private String colorName;
    private String currentSizeId;
    private String currentSizeName;
    private String requestedSizeId;
    private String requestedSizeName;
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String transferContent;
    private String adminNote;
    private String rejectedReason;
    private String provider;
    private String refundTransactionId;
    private String providerResponseCode;
    private String providerResponseMessage;
    private RefundStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime receivedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
