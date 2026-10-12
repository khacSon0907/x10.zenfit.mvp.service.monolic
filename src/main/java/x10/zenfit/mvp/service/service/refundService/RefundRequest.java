package x10.zenfit.mvp.service.service.refundService;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.RefundType;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RefundRequest {

    @NotBlank(message = "Order id is required")
    private String orderId;

    private RefundType type = RefundType.RETURN;

    private BigDecimal refundAmount;

    private String reason;

    private List<String> imageUrls;

    private String productId;

    private String currentSizeId;

    private String requestedSizeId;

    @NotBlank(message = "Bank code is required")
    private String bankCode;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account name is required")
    private String accountName;
}
