package x10.zenfit.mvp.service.service.addressService.updateAddressUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IAddressRepository;
import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;
import x10.zenfit.mvp.service.service.addressService.AddressResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.address.AddressError;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateAddressUcImpl implements IUpdateAddressUc {

    private final IAddressRepository addressRepository;

    @Override
    public AddressResponse execute(String userId, String addressId, UpdateAddressRequest request) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(AddressError.ADDRESS_NOT_FOUND));

        if (!address.getUserId().equals(userId)) {
            throw new BusinessException(AddressError.ADDRESS_NOT_FOUND);
        }

        boolean wasDefault = address.isDefault();
        boolean setAsDefault = request.isDefault();

        if (setAsDefault && !wasDefault) {
            // Unset other default addresses
            List<AddressEntity> userAddresses = addressRepository.findByUserId(userId);
            for (AddressEntity addr : userAddresses) {
                if (addr.isDefault() && !addr.getId().equals(addressId)) {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                }
            }
            address.setDefault(true);
        } else if (!setAsDefault && wasDefault) {
            // If they try to turn off default status on the default address, 
            // check if other addresses exist to transfer default status to.
            List<AddressEntity> userAddresses = addressRepository.findByUserId(userId);
            AddressEntity otherAddress = userAddresses.stream()
                    .filter(addr -> !addr.getId().equals(addressId))
                    .findFirst()
                    .orElse(null);

            if (otherAddress != null) {
                otherAddress.setDefault(true);
                addressRepository.save(otherAddress);
                address.setDefault(false);
            } else {
                // Keep it default if it's the only address
                address.setDefault(true);
            }
        }

        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvinceCode(request.getProvinceCode());
        address.setDistrictCode(request.getDistrictCode());
        address.setWardCode(request.getWardCode());
        address.setProvinceName(request.getProvinceName());
        address.setDistrictName(request.getDistrictName());
        address.setWardName(request.getWardName());
        address.setStreetAddress(request.getStreetAddress());

        AddressEntity savedAddress = addressRepository.save(address);

        return AddressResponse.builder()
                .id(savedAddress.getId())
                .userId(savedAddress.getUserId())
                .receiverName(savedAddress.getReceiverName())
                .receiverPhone(savedAddress.getReceiverPhone())
                .provinceCode(savedAddress.getProvinceCode())
                .districtCode(savedAddress.getDistrictCode())
                .wardCode(savedAddress.getWardCode())
                .provinceName(savedAddress.getProvinceName())
                .districtName(savedAddress.getDistrictName())
                .wardName(savedAddress.getWardName())
                .streetAddress(savedAddress.getStreetAddress())
                .isDefault(savedAddress.isDefault())
                .build();
    }
}
