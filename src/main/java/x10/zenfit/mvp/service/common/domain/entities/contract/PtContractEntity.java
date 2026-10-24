package x10.zenfit.mvp.service.common.domain.entities.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;

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

    private String goal;               // String, PT tự điền
    private Double targetWeight;
    private Integer totalSessions;     // số buổi mua
    private Integer bonusSessions;     // buổi tặng
    private Integer usedSessions;      // đã tập (counter)
    // remaining = totalSessions + bonusSessions - usedSessions

    private LocalDate startDate;
    private LocalDate endDate;         // hạn sử dụng gói

    private BigDecimal price;
    private BigDecimal paidAmount;

    private ContractStatus status;     // ACTIVE, COMPLETED, EXPIRED, FROZEN, CANCELLED
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public int getRemainingSessions() {
        int total = totalSessions != null ? totalSessions : 0;
        int bonus = bonusSessions != null ? bonusSessions : 0;
        int used = usedSessions != null ? usedSessions : 0;
        return total + bonus - used;
    }
}