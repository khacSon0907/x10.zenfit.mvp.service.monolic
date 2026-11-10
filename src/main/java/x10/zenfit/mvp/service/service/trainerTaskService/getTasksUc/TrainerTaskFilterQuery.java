package x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTaskFilterQuery {

    private String trainerId;
    private LocalDate taskDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private TrainerTaskStatus status;
}

