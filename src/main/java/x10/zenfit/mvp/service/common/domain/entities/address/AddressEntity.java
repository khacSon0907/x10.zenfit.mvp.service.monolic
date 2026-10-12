package x10.zenfit.mvp.service.common.domain.entities.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

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