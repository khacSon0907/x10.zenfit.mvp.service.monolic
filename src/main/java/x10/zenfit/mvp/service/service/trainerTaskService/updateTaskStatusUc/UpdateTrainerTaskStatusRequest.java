package x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerTaskStatusRequest {

    @NotNull(message = "Trạng thái công việc không được để trống")
    private TrainerTaskStatus status;
}

