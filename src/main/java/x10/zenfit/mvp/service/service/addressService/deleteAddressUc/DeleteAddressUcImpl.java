package x10.zenfit.mvp.service.service.addressService.deleteAddressUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IAddressRepository;
import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.address.AddressError;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteAddressUcImpl implements IDeleteAddressUc {

    private final IAddressRepository addressRepository;

    @Override
    public void execute(String userId, String addressId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(AddressError.ADDRESS_NOT_FOUND));

        if (!address.getUserId().equals(userId)) {
            throw new BusinessException(AddressError.ADDRESS_NOT_FOUND);
        }

        if (address.isDefault()) {
            List<AddressEntity> otherAddresses = addressRepository.findByUserId(userId).stream()
                    .filter(addr -> !addr.getId().equals(addressId))
                    .toList();

            if (!otherAddresses.isEmpty()) {
                AddressEntity newDefault = otherAddresses.get(0);
                newDefault.setDefault(true);
                addressRepository.save(newDefault);
            }
        }

        addressRepository.deleteById(addressId);
    }
}
