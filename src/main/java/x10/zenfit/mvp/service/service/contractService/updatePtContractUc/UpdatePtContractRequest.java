package x10.zenfit.mvp.service.service.contractService.updatePtContractUc;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePtContractRequest {

    // Tổng số buổi mua
    @Min(value = 1, message = "Tổng số buổi tập phải từ 1 buổi trở lên")
    private Integer totalSessions;

    // Số buổi tặng
    @Min(value = 0, message = "Số buổi tặng không được âm")
    private Integer bonusSessions;

    // Số buổi đã tập
    @Min(value = 0, message = "Số buổi đã tập không được âm")
    private Integer usedSessions;

    // Ngày bắt đầu
    private LocalDate startDate;

    // Ngày hết hạn
    private LocalDate endDate;

    // Giá trị hợp đồng
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Giá trị hợp đồng phải lớn hơn 0"
    )
    private BigDecimal price;

    // Giá mỗi buổi tập
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Giá mỗi buổi tập phải lớn hơn 0"
    )
    private BigDecimal pricePerSession;

    // Số tiền đã thanh toán
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Số tiền thanh toán không được âm"
    )
    private BigDecimal paidAmount;

    // Trạng thái hợp đồng
    private ContractStatus status;

    // Trạng thái thanh toán của hợp đồng
    private PaymentContractStatus paymentContractStatus;

    // Ghi chú
    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String notes;
}

