package x10.zenfit.mvp.service.common.domain.entities.guest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestEntity {

    private String id;

    private String email;

    private String username;

    private LocalDateTime createdAt;
}
