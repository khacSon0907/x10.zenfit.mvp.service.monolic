
package x10.zenfit.mvp.service.common.domain.entities.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtContractEntity {

    private String id;

    private String clientId;

    private String trainerId;

    // Session information
    private Integer totalSessions;     // Số buổi mua
    private Integer bonusSessions;     // Số buổi tặng
    private Integer usedSessions;      // Số buổi đã tập

    // Financial information
    private BigDecimal price;                  // Tổng giá trị hợp đồng
    private BigDecimal pricePerSession;        // Giá tiền một buổi tập
    private BigDecimal paidAmount;             // Số tiền đã thanh toán

    // Contract duration
    private LocalDate startDate;
    private LocalDate endDate;

    // Contract status
    private ContractStatus status;

    // Contract payment status
    private PaymentContractStatus paymentContractStatus;

    private String notes;

    // Audit information
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Calculate remaining sessions.
     * Remaining = Total + Bonus - Used
     */
    public int getRemainingSessions() {
        int total = totalSessions != null ? totalSessions : 0;
        int bonus = bonusSessions != null ? bonusSessions : 0;
        int used = usedSessions != null ? usedSessions : 0;

        return Math.max(0, total + bonus - used);
    }
}
