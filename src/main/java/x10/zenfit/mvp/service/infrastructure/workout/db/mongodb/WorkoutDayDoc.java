package x10.zenfit.mvp.service.infrastructure.workout.db.mongodb;

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
public class WorkoutDayDoc {

    private String id;
    private Integer dayNumber;
    private String dayName;
    private String description;

    @Builder.Default
    private List<WorkoutExerciseDoc> exercises = new ArrayList<>();
}

