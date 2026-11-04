package x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionFilterQuery {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String trainerId;
    private String clientId;
    private String contractId;
    private TrainingSessionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}

