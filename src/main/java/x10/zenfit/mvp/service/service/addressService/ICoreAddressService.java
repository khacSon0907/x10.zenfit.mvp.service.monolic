package x10.zenfit.mvp.service.service.addressService;

import x10.zenfit.mvp.service.service.addressService.createAddressUc.CreateAddressRequest;
import x10.zenfit.mvp.service.service.addressService.updateAddressUc.UpdateAddressRequest;

import java.util.List;

public interface ICoreAddressService {
    AddressResponse createAddress(String userId, CreateAddressRequest request);
    AddressResponse updateAddress(String userId, String addressId, UpdateAddressRequest request);
    AddressResponse getAddress(String userId, String addressId);
    List<AddressResponse> getAddressesByUserId(String userId);
    void deleteAddress(String userId, String addressId);
}
