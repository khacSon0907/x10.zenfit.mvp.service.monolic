package x10.zenfit.mvp.service.common.domain.entities.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTaskEntity {
    private String id;
    private String trainerId;          // PT sở hữu việc này

    private String title;              // "Thức dậy", "Ăn sáng"
    private String description;
    private String color;              // màu hiển thị trên lịch (tuỳ chọn)

    private LocalDate taskDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean allDay;

    private TrainerTaskStatus status;  // TODO, DONE, SKIPPED
    private LocalDateTime completedAt;



    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}