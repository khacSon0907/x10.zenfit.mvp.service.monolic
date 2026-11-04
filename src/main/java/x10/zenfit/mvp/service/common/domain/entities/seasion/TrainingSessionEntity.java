
package x10.zenfit.mvp.service.common.domain.entities.seasion;

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

    // ID huấn luyện viên phụ trách buổi tập
    private String trainerId;

    // ID hợp đồng PT liên kết với buổi tập
    private String contractId;

    // Tên hoặc nội dung buổi tập
    private String sessionName;

    // Ngày diễn ra buổi tập
    private LocalDate trainingDate;

    // Thời gian bắt đầu buổi tập
    private LocalTime startTime;

    // Thời điểm kết thúc buổi tập
    private LocalDateTime endTime;

    // Trạng thái hiện tại của buổi tập
    private TrainingSessionStatus status;

    // Thời điểm PT thực hiện check-in
    private LocalDateTime checkInAt;

    // Thời điểm hoàn thành buổi tập
    private LocalDateTime completedAt;

    // Danh sách URL ảnh minh chứng buổi tập
    private List<String> evidenceImageUrls;

    // Ghi chú bổ sung của PT
    private String note;

    // Thời điểm tạo buổi tập
    private LocalDateTime createdAt;

    // Thời điểm cập nhật gần nhất
    private LocalDateTime updatedAt;
}