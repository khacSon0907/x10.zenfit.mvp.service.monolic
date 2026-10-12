package x10.zenfit.mvp.service.service.addressService.getAddressUc;

import x10.zenfit.mvp.service.service.addressService.AddressResponse;

public interface IGetAddressUc {
    AddressResponse execute(String userId, String addressId);
}
