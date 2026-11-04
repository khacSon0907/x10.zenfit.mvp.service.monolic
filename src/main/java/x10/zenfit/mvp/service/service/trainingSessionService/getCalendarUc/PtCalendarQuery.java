package x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc;

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
public class PtCalendarQuery {

    private CalendarViewType view;
    private LocalDate date;
    private String trainerId;
    private String clientId;
    private String contractId;
    private TrainingSessionStatus status;
}

