package x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc;

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
public class UpdateTrainingSessionRequest {

    private String clientId;

    private String trainerId;

    private String contractId;

    @Size(max = 100, message = "Tên buổi tập tối đa 100 ký tự")
    private String sessionName;

    private LocalDate trainingDate;

    private LocalTime startTime;

    private LocalDateTime endTime;

    private List<String> evidenceImageUrls;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String note;
}

