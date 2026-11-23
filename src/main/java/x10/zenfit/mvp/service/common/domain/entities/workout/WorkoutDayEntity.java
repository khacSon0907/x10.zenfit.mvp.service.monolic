package x10.zenfit.mvp.service.common.domain.entities.workout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDayEntity {

    private String id;

    // Thứ tự ngày tập (ví dụ: 1, 2, 3...)
    private Integer dayNumber;

    // Tên buổi tập (ví dụ: Chest & Triceps, Leg Day, Ngày 1)
    private String dayName;

    // Mô tả buổi tập
    private String description;

    // Danh sách bài tập trong ngày
    @Builder.Default
    private List<WorkoutExerciseEntity> exercises = new ArrayList<>();
}

