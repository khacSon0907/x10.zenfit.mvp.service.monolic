package x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionCalendarItemResponse {

    private String id;
    private String clientId;
    private String trainerId;
    private String contractId;
    private String sessionName;
    private LocalDate trainingDate;
    private LocalTime startTime;
    private LocalDateTime endTime;
    private TrainingSessionStatus status;
    private List<String> evidenceImageUrls;
    private String note;

    public static TrainingSessionCalendarItemResponse fromEntity(TrainingSessionEntity entity) {
        if (entity == null) {
            return null;
        }

        return TrainingSessionCalendarItemResponse.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .trainerId(entity.getTrainerId())
                .contractId(entity.getContractId())
                .sessionName(entity.getSessionName())
                .trainingDate(entity.getTrainingDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .status(entity.getStatus())
                .evidenceImageUrls(entity.getEvidenceImageUrls())
                .note(entity.getNote())
                .build();
    }
}

