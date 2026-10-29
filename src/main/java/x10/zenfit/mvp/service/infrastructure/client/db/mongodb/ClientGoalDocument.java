package x10.zenfit.mvp.service.infrastructure.client.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "client_goals")
public class ClientGoalDocument {

    @Id
    private String id;
    private String clientId;

    private String goalType;
    private LocalDate startDate;
    private LocalDate endDate;

    private Double startWeight;
    private Double targetWeight;
    private Double endWeight;

    private GoalPhaseStatus status;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
