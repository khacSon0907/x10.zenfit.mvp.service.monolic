package x10.zenfit.mvp.service.common.domain.entities.workout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanEntity {

    private String id;

    // PT phụ trách
    private String trainerId;

    // Khách hàng nhận giáo án
    private String clientId;

    // Tên giáo án
    private String name;

    // Mô tả chi tiết
    private String description;

    // Thời gian áp dụng
    private LocalDate startDate;
    private LocalDate endDate;

    // Trạng thái giáo án
    private WorkoutPlanStatus status;

    // Danh sách các ngày tập (3 ngày, 4 ngày, theo tuần...)
    @Builder.Default
    private List<WorkoutDayEntity> trainingDays = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

