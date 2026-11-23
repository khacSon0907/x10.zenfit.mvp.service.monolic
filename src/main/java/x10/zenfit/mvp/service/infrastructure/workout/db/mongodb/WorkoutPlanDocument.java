package x10.zenfit.mvp.service.infrastructure.workout.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "workout_plans")
public class WorkoutPlanDocument {

    @Id
    private String id;

    private String trainerId;
    private String clientId;

    private String name;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private WorkoutPlanStatus status;

    @Builder.Default
    private List<WorkoutDayDoc> trainingDays = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

