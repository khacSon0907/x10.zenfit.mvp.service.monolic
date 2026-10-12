package x10.zenfit.mvp.service.service.addressService.getAddressUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IAddressRepository;
import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;
import x10.zenfit.mvp.service.service.addressService.AddressResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.address.AddressError;

@Component
@RequiredArgsConstructor
public class GetAddressUcImpl implements IGetAddressUc {

    private final IAddressRepository addressRepository;

    @Override
    public AddressResponse execute(String userId, String addressId) {
        AddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(AddressError.ADDRESS_NOT_FOUND));

        if (!address.getUserId().equals(userId)) {
            throw new BusinessException(AddressError.ADDRESS_NOT_FOUND);
        }

        return AddressResponse.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .provinceCode(address.getProvinceCode())
                .districtCode(address.getDistrictCode())
                .wardCode(address.getWardCode())
                .provinceName(address.getProvinceName())
                .districtName(address.getDistrictName())
                .wardName(address.getWardName())
                .streetAddress(address.getStreetAddress())
                .isDefault(address.isDefault())
                .build();
    }
}
