package x10.zenfit.mvp.service.service.contractService.updatePtContractUc;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePtContractRequest {

    private String trainerId;

    // Mục tiêu tập luyện nếu cần cập nhật
    private String goalType;
    private Double targetWeight;
    private String goalNote;

    @Min(value = 1, message = "Tổng số buổi tập phải từ 1 buổi trở lên")
    private Integer totalSessions;

    @Min(value = 0, message = "Số buổi tặng không được âm")
    private Integer bonusSessions;

    @Min(value = 0, message = "Số buổi đã tập không được âm")
    private Integer usedSessions;

    private LocalDate startDate;
    private LocalDate endDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Giá trị hợp đồng không được âm")
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = true, message = "Số tiền đã thanh toán không được âm")
    private BigDecimal paidAmount;

    private ContractStatus status;
    private String notes;
}
