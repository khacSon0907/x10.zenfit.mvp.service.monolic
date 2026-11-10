package x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerTaskRequest {

    private String trainerId;

    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    private String title;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    @Size(max = 30, message = "Màu sắc tối đa 30 ký tự")
    private String color;

    private LocalDate taskDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean allDay;

    private TrainerTaskStatus status;
}

