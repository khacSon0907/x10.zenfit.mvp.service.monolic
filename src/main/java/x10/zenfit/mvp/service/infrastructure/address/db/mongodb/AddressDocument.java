package x10.zenfit.mvp.service.infrastructure.address.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "addresses")
public class AddressDocument {

    @Id
    private String id;

    private String userId;

    private String receiverName;

    private String receiverPhone;

    private String provinceCode;
    private String districtCode;
    private String wardCode;

    private String provinceName;
    private String districtName;
    private String wardName;

    private String streetAddress;

    private boolean isDefault;
}
