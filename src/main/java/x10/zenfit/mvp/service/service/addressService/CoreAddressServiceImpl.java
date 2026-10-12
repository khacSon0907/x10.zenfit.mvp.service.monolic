package x10.zenfit.mvp.service.service.addressService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.addressService.createAddressUc.CreateAddressRequest;
import x10.zenfit.mvp.service.service.addressService.createAddressUc.ICreateAddressUc;
import x10.zenfit.mvp.service.service.addressService.deleteAddressUc.IDeleteAddressUc;
import x10.zenfit.mvp.service.service.addressService.getAddressUc.IGetAddressUc;
import x10.zenfit.mvp.service.service.addressService.getAddressesByUserIdUc.IGetAddressesByUserIdUc;
import x10.zenfit.mvp.service.service.addressService.updateAddressUc.IUpdateAddressUc;
import x10.zenfit.mvp.service.service.addressService.updateAddressUc.UpdateAddressRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreAddressServiceImpl implements ICoreAddressService {

    private final ICreateAddressUc createAddressUc;
    private final IUpdateAddressUc updateAddressUc;
    private final IGetAddressUc getAddressUc;
    private final IGetAddressesByUserIdUc getAddressesByUserIdUc;
    private final IDeleteAddressUc deleteAddressUc;

    @Override
    public AddressResponse createAddress(String userId, CreateAddressRequest request) {
        return createAddressUc.execute(userId, request);
    }

    @Override
    public AddressResponse updateAddress(String userId, String addressId, UpdateAddressRequest request) {
        return updateAddressUc.execute(userId, addressId, request);
    }

    @Override
    public AddressResponse getAddress(String userId, String addressId) {
        return getAddressUc.execute(userId, addressId);
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(String userId) {
        return getAddressesByUserIdUc.execute(userId);
    }

    @Override
    public void deleteAddress(String userId, String addressId) {
        deleteAddressUc.execute(userId, addressId);
    }
}
