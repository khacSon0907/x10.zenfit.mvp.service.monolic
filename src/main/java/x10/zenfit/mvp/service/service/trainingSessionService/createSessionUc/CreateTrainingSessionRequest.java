package x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingSessionRequest {

    @NotBlank(message = "Mã học viên không được để trống")
    private String clientId;

    private String trainerId; // Tùy chọn nếu PT đăng nhập; bắt buộc nếu ADMIN tạo cho PT khác

    @NotBlank(message = "Mã hợp đồng không được để trống")
    private String contractId;

    @NotBlank(message = "Tên buổi tập không được để trống")
    @Size(max = 100, message = "Tên buổi tập tối đa 100 ký tự")
    private String sessionName;

    @NotNull(message = "Ngày tập không được để trống")
    private LocalDate trainingDate;

    @NotNull(message = "Giờ bắt đầu không được để trống")
    private LocalTime startTime;

    // Thời gian kết thúc (tùy chọn; nếu không truyền sẽ tự động mặc định = startTime + 1 giờ)
    private LocalDateTime endTime;

    // Danh sách URL ảnh minh chứng buổi tập (tùy chọn)
    private List<String> evidenceImageUrls;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String note;
}

