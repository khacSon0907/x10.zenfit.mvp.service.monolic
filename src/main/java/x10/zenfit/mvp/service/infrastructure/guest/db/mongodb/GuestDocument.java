package x10.zenfit.mvp.service.infrastructure.guest.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "guests")
public class GuestDocument {

    @Id
    private String id;

    private String email;

    private String username;

    private LocalDateTime createdAt;
}
