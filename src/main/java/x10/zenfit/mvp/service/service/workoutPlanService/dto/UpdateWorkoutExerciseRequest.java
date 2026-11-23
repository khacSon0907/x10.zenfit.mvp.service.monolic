package x10.zenfit.mvp.service.service.workoutPlanService.dto;

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
public class UpdateWorkoutExerciseRequest {

    private String exerciseName;

    @Positive(message = "Số hiệp (sets) phải lớn hơn 0")
    private Integer sets;

    @Positive(message = "Số lần mỗi hiệp (reps) phải lớn hơn 0")
    private Integer reps;

    @PositiveOrZero(message = "Thời gian nghỉ (restSeconds) không được âm")
    private Integer restSeconds;

    private String tempo;

    private String note;
}

