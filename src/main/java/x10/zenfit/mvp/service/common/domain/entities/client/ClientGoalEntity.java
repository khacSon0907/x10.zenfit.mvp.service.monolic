package x10.zenfit.mvp.service.common.domain.entities.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientGoalEntity {

    private String id;

    private String clientId;

    private String trainerId;

    private String goalName;

    private String goalType;
    // BULKING / CUTTING / MAINTAINING

    private LocalDate startDate;

    private LocalDate endDate;
    // Ngày kết thúc dự kiến

    private LocalDate actualEndDate;
    // Ngày kết thúc thực tế

    private Double startWeight;
    // Cân nặng bắt đầu (kg)

    private Double targetWeight;
    // Cân nặng mục tiêu (kg)

    private Double endWeight;
    // Cân nặng thực tế khi kết thúc (kg)

    private GoalPhaseStatus status;
    // PLANNED / ACTIVE / COMPLETED / CANCELLED

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}