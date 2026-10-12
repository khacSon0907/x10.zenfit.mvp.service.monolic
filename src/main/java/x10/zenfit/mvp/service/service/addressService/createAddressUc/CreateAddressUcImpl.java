package x10.zenfit.mvp.service.service.addressService.createAddressUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IAddressRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;
import x10.zenfit.mvp.service.service.addressService.AddressResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAddressUcImpl implements ICreateAddressUc {

    private final IAddressRepository addressRepository;
    private final IUserRepository userRepository;

    @Override
    public AddressResponse execute(String userId, CreateAddressRequest request) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        List<AddressEntity> existingAddresses = addressRepository.findByUserId(userId);
        boolean isDefault = request.isDefault();

        // If it's the first address, it must be default
        if (existingAddresses.isEmpty()) {
            isDefault = true;
        } else if (isDefault) {
            // Unset previous default addresses
            for (AddressEntity addr : existingAddresses) {
                if (addr.isDefault()) {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                }
            }
        }

        AddressEntity newAddress = AddressEntity.builder()
                .userId(userId)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .provinceCode(request.getProvinceCode())
                .districtCode(request.getDistrictCode())
                .wardCode(request.getWardCode())
                .provinceName(request.getProvinceName())
                .districtName(request.getDistrictName())
                .wardName(request.getWardName())
                .streetAddress(request.getStreetAddress())
                .isDefault(isDefault)
                .build();

        AddressEntity savedAddress = addressRepository.save(newAddress);

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
