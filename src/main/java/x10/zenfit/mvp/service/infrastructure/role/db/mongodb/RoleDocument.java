package x10.zenfit.mvp.service.infrastructure.role.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "roles")
public class RoleDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String name;

    private String description;

    private boolean active;
}
