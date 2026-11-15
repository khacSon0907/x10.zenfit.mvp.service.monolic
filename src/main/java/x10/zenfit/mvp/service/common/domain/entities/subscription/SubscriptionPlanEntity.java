
package x10.zenfit.mvp.service.common.domain.entities.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanEntity {

    private String id;

    // Thông tin gói
    private String code;
    private String name;
    private String description;

    // Giá gói
    private BigDecimal price;

    // Chu kỳ thanh toán (ngày)
    private Integer durationDays;

    // Giới hạn học viên
    private Integer maxStudents;

    // Giới hạn PT (nếu cần mở rộng)
    private Integer maxTrainingSessions;

    // Trạng thái gói
    private SubscriptionPlanStatus status;

    // Gói mặc định miễn phí
    private Boolean isDefault;

    // Thứ tự hiển thị
    private Integer displayOrder;

    // Thời gian tạo và cập nhật
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
