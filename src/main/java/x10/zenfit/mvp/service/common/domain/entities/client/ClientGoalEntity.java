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

    private String goalType;        // BULKING / CUTTING / ...
    private LocalDate startDate;
    private LocalDate endDate;           // ngày kết thúc dự kiến (hoặc thực tế)

    private Double startWeight;          // kg, cân nặng đầu giai đoạn
    private Double targetWeight;         // kg, mục tiêu của giai đoạn
    private Double endWeight;            // kg, cân nặng thực tế khi kết thúc

    private GoalPhaseStatus status;      // PLANNED / ACTIVE / COMPLETED / CANCELLED
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}