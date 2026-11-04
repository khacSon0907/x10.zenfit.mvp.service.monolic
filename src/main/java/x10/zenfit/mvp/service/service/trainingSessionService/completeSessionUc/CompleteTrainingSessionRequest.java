package x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTrainingSessionRequest {

    // Danh sách URL ảnh minh chứng buổi tập sau khi upload Cloudinary
    private List<String> evidenceImageUrls;

    // Ghi chú bổ sung (tùy chọn)
    private String note;
}

