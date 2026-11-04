package x10.zenfit.mvp.service.infrastructure.session.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "training_sessions")
@CompoundIndexes({
        @CompoundIndex(name = "trainer_datetime_idx", def = "{'trainerId': 1, 'trainingDate': 1, 'startTime': 1}"),
        @CompoundIndex(name = "client_datetime_idx", def = "{'clientId': 1, 'trainingDate': 1, 'startTime': 1}"),
        @CompoundIndex(name = "trainer_date_idx", def = "{'trainerId': 1, 'trainingDate': 1}")
})
public class TrainingSessionDocument {

    @Id
    private String id;

    @Indexed
    private String clientId;

    @Indexed
    private String trainerId;

    @Indexed
    private String contractId;

    private String sessionName;

    @Indexed
    private LocalDate trainingDate;

    private LocalTime startTime;

    private LocalDateTime endTime;

    private TrainingSessionStatus status;

    private LocalDateTime checkInAt;

    private LocalDateTime completedAt;

    private List<String> evidenceImageUrls;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

