package x10.zenfit.mvp.service.service.addressService.getAddressesByUserIdUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IAddressRepository;
import x10.zenfit.mvp.service.service.addressService.AddressResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAddressesByUserIdUcImpl implements IGetAddressesByUserIdUc {

    private final IAddressRepository addressRepository;

    @Override
    public List<AddressResponse> execute(String userId) {
        return addressRepository.findByUserId(userId)
                .stream()
                .map(address -> AddressResponse.builder()
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
                        .build())
                .toList();
    }
}
