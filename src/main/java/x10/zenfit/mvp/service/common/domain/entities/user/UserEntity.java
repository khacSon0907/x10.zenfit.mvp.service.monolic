package x10.zenfit.mvp.service.common.domain.entities.user;

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
public class UserEntity {

    private String id;

    private String username;

    private String email;

    private String passwordHash;

    private String phoneNumber;

    private String avatarUrl;

    private LocalDate birthday;

    private LocalDateTime birthdayLastUpdatedAt;

    private Integer birthdayUpdateCount;

    private Long points;

    private UserStatus status;

    private AuthProvider providerType;

    private boolean emailVerified;

    private LocalDateTime verifiedAt;

    private List<String> roleIds;

    private Instant createdAt;

    private Instant updatedAt;
}
