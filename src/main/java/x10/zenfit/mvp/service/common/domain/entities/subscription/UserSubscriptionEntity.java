
package x10.zenfit.mvp.service.common.domain.entities.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionEntity {

    private String id;

    // Người sở hữu gói
    private String userId;

    // Gói đăng ký
    private String planId;

    // Trạng thái đăng ký
    private UserSubscriptionStatus status;

    // Thời điểm bắt đầu
    private Instant startAt;

    // Thời điểm hết hạn
    private Instant endAt;

    // Giá gói tại thời điểm đăng ký
    private BigDecimal price;

    // Giới hạn học viên tại thời điểm đăng ký
    private Integer maxStudents;

    // Số học viên đã sử dụng
    private Integer currentStudents;

    // Tự động gia hạn hay không
    private Boolean autoRenew;

    // Thời điểm thanh toán
    private Instant paidAt;

    // ID giao dịch thanh toán
    private String transactionId;

    // Thời gian tạo và cập nhật
    private Instant createdAt;
    private Instant updatedAt;
}
