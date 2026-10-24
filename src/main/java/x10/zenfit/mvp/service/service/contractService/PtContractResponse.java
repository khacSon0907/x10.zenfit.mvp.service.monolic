package x10.zenfit.mvp.service.service.contractService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
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

    // Chi tiết thông tin khách hàng đi kèm (nếu tìm thấy)
    private ClientResponse client;

    private String goal;
    private Double targetWeight;
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
