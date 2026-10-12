package x10.zenfit.mvp.service.service.addressService.createAddressUc;

import x10.zenfit.mvp.service.service.addressService.AddressResponse;

public interface ICreateAddressUc {
    AddressResponse execute(String userId, CreateAddressRequest request);
}
