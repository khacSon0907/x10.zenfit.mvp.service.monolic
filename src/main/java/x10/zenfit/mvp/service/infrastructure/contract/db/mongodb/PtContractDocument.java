package x10.zenfit.mvp.service.infrastructure.contract.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "pt_contracts")
public class PtContractDocument {

    @Id
    private String id;
    private String clientId;
    private String trainerId;

    private Integer totalSessions;
    private Integer bonusSessions;
    private Integer usedSessions;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal price;
    private BigDecimal paidAmount;

    private ContractStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
