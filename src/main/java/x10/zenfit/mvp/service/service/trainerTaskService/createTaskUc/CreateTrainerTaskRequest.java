package x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainerTaskRequest {

    // Cho phép ADMIN, TEAM_MANAGER, HEAD_OF_TRAINING gán cho PT khác; nếu không truyền thì lấy PT hiện tại
    private String trainerId;

    @NotBlank(message = "Tiêu đề công việc / hoạt động không được để trống")
    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    private String title;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    @Size(max = 30, message = "Màu sắc tối đa 30 ký tự")
    private String color;

    @NotNull(message = "Ngày thực hiện không được để trống")
    private LocalDate taskDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean allDay;
}

