package x10.zenfit.mvp.service.common.domain.entities.workout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseEntity {

    private String id;

    // Tên bài tập
    private String exerciseName;

    // Số hiệp (Sets)
    private Integer sets;

    // Số lần thực hiện mỗi hiệp (Reps)
    private Integer reps;

    // Thời gian nghỉ giữa hiệp (giây)
    private Integer restSeconds;

    // Nhịp độ thực hiện (ví dụ: 3-1-1-0)
    private String tempo;

    // Ghi chú hướng dẫn
    private String note;
}

