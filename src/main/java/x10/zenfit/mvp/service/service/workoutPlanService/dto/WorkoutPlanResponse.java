package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanResponse {

    private String id;
    private String trainerId;
    private String clientId;
    private String clientName;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private WorkoutPlanStatus status;

    @Builder.Default
    private List<WorkoutDayResponse> trainingDays = new ArrayList<>();

    private Integer totalDays;
    private Integer totalExercises;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WorkoutPlanResponse fromEntity(WorkoutPlanEntity entity, String clientName) {
        if (entity == null) {
            return null;
        }

        List<WorkoutDayResponse> dayResponses = new ArrayList<>();
        int exercisesCount = 0;

        if (entity.getTrainingDays() != null) {
            dayResponses = entity.getTrainingDays().stream()
                    .map(WorkoutDayResponse::fromEntity)
                    .toList();

            for (var day : entity.getTrainingDays()) {
                if (day.getExercises() != null) {
                    exercisesCount += day.getExercises().size();
                }
            }
        }

        return WorkoutPlanResponse.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainerId())
                .clientId(entity.getClientId())
                .clientName(clientName)
                .name(entity.getName())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .trainingDays(dayResponses)
                .totalDays(dayResponses.size())
                .totalExercises(exercisesCount)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

