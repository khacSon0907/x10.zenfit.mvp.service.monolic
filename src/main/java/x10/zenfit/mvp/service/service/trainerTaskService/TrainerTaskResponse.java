package x10.zenfit.mvp.service.service.trainerTaskService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTaskResponse {

    private String id;
    private String trainerId;
    private String title;
    private String description;
    private String color;
    private LocalDate taskDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean allDay;
    private TrainerTaskStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TrainerTaskResponse fromEntity(TrainerTaskEntity entity) {
        if (entity == null) {
            return null;
        }

        return TrainerTaskResponse.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainerId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .color(entity.getColor())
                .taskDate(entity.getTaskDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .allDay(entity.isAllDay())
                .status(entity.getStatus())
                .completedAt(entity.getCompletedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

