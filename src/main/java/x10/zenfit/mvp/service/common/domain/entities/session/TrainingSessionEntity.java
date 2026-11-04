package x10.zenfit.mvp.service.common.domain.entities.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionEntity {

    // ID định danh duy nhất của buổi tập
    private String id;

    // ID học viên tham gia buổi tập
    private String clientId;

    // ID huấn luyện viên (PT) phụ trách buổi tập
    private String trainerId;

    // ID hợp đồng PT liên kết với buổi tập này
    private String contractId;

    // Tên hoặc nội dung chính của buổi tập
    // Ví dụ: Upper Body, Lower Body, Full Body
    private String sessionName;

    // Ngày diễn ra buổi tập
    private LocalDate trainingDate;

    // Thời gian bắt đầu buổi tập
    private LocalTime startTime;

    // Thời gian kết thúc buổi tập
    private LocalDateTime endTime;

    // Trạng thái hiện tại của buổi tập
    // SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    private TrainingSessionStatus status;

    // Thời điểm học viên thực hiện check-in
    // Null nếu học viên chưa check-in
    private LocalDateTime checkInAt;

    // Thời điểm buổi tập được xác nhận là đã hoàn thành
    // Null nếu buổi tập chưa hoàn thành
    private LocalDateTime completedAt;

    // Danh sách URL ảnh minh chứng buổi tập (do PT tải lên sau khi check-in hoặc hoàn thành)
    private List<String> evidenceImageUrls;

    // Ghi chú bổ sung của PT về buổi tập
    private String note;

    // Thời điểm tạo thông tin buổi tập trong hệ thống
    private LocalDateTime createdAt;

    // Thời điểm cập nhật thông tin buổi tập gần nhất
    private LocalDateTime updatedAt;
}

