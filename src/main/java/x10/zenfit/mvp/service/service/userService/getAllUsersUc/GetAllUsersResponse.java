package x10.zenfit.mvp.service.service.userService.getAllUsersUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.AuthProvider;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersResponse {

    private String id;

    private String username;

    private String email;

    private String phoneNumber;

    private UserStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean emailVerified;

    private LocalDateTime verifiedAt;

    private String avatarUrl;

    private LocalDate birthday;

    private Integer birthdayUpdateCount;

    private Long points;

    private LocalDateTime birthdayLastUpdatedAt;

    private LocalDateTime nextBirthdayUpdateAt;

    private boolean canUpdateBirthday;

    private AuthProvider providerType;

    // ROLE
    private List<String> roleIds;
}
