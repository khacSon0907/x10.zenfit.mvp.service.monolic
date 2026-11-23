package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseRequest {

    @NotBlank(message = "Tên bài tập không được để trống")
    private String exerciseName;

    @NotNull(message = "Số hiệp (sets) không được để trống")
    @Positive(message = "Số hiệp (sets) phải lớn hơn 0")
    private Integer sets;

    @NotNull(message = "Số lần mỗi hiệp (reps) không được để trống")
    @Positive(message = "Số lần mỗi hiệp (reps) phải lớn hơn 0")
    private Integer reps;

    @NotNull(message = "Thời gian nghỉ giữa hiệp không được để trống")
    @PositiveOrZero(message = "Thời gian nghỉ (restSeconds) không được âm")
    private Integer restSeconds;

    @NotBlank(message = "Tempo không được để trống (ví dụ: 3-1-1-0)")
    private String tempo;

    private String note;
}

