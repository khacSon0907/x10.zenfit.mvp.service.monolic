package x10.zenfit.mvp.service.infrastructure.user.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.AuthProvider;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String username;

    private String email;

    private String phoneNumber;

    private String passwordHash;

    private UserStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean emailVerified;

    private LocalDateTime verifiedAt;

    private String avatarUrl;

    private LocalDate birthday;

    private LocalDateTime birthdayLastUpdatedAt;

    private Integer birthdayUpdateCount;

    private Long points;

    private AuthProvider providerType;

    // ROLE
    private List<String> roleIds;

    @Deprecated
    @Field("roles")
    private List<String> legacyRoles;
}
