package x10.zenfit.mvp.service.common.domain.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBirthdayHistoryEntity {
    private String id;
    private String userId;
    private LocalDate oldBirthday;
    private LocalDate newBirthday;
    private String changedByUserId;
    private boolean changedByAdmin;
    private LocalDateTime changedAt;
}
