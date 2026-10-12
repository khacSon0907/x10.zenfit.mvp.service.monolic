package x10.zenfit.mvp.service.service.addressService.updateAddressUc;

import x10.zenfit.mvp.service.service.addressService.AddressResponse;

public interface IUpdateAddressUc {
    AddressResponse execute(String userId, String addressId, UpdateAddressRequest request);
}
