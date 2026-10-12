package x10.zenfit.mvp.service.service.addressService.getAddressesByUserIdUc;

import x10.zenfit.mvp.service.service.addressService.AddressResponse;
import java.util.List;

public interface IGetAddressesByUserIdUc {
    List<AddressResponse> execute(String userId);
}
