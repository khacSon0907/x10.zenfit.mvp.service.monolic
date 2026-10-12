package x10.zenfit.mvp.service.infrastructure.user.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_birthday_histories")
public class UserBirthdayHistoryDocument {
    @Id
    private String id;
    private String userId;
    private LocalDate oldBirthday;
    private LocalDate newBirthday;
    private String changedByUserId;
    private boolean changedByAdmin;
    private LocalDateTime changedAt;
}
