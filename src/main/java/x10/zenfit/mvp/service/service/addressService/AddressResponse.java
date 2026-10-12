package x10.zenfit.mvp.service.service.addressService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
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
