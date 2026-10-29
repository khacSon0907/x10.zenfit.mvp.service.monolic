package x10.zenfit.mvp.service.service.contractService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.clientService.ClientGoalResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtContractResponse {

    private String id;
    private String clientId;
    private String trainerId;

    // Chi tiết thông tin khách hàng đi kèm (nếu có)
    private ClientResponse client;

    // Chi tiết mục tiêu khách hàng đi kèm (nếu có)
    private ClientGoalResponse goal;

    private Integer totalSessions;
    private Integer bonusSessions;
    private Integer usedSessions;
    private Integer remainingSessions;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal price;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;

    private ContractStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
