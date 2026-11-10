package x10.zenfit.mvp.service.infrastructure.trainer.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trainer_tasks")
@CompoundIndexes({
        @CompoundIndex(name = "trainer_date_idx", def = "{'trainerId': 1, 'taskDate': 1}"),
        @CompoundIndex(name = "trainer_status_idx", def = "{'trainerId': 1, 'status': 1}")
})
public class TrainerTaskDocument {

    @Id
    private String id;

    @Indexed
    private String trainerId;

    private String title;

    private String description;

    private String color;

    @Indexed
    private LocalDate taskDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean allDay;

    private TrainerTaskStatus status;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

