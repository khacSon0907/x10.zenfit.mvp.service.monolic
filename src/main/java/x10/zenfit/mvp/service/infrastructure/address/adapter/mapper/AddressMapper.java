package x10.zenfit.mvp.service.infrastructure.address.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;
import x10.zenfit.mvp.service.infrastructure.address.db.mongodb.AddressDocument;

public class AddressMapper {

    public static AddressEntity toEntity(AddressDocument document) {
        if (document == null) {
            return null;
        }

        return AddressEntity.builder()
                .id(document.getId())
                .userId(document.getUserId())
                .receiverName(document.getReceiverName())
                .receiverPhone(document.getReceiverPhone())
                .provinceCode(document.getProvinceCode())
                .districtCode(document.getDistrictCode())
                .wardCode(document.getWardCode())
                .provinceName(document.getProvinceName())
                .districtName(document.getDistrictName())
                .wardName(document.getWardName())
                .streetAddress(document.getStreetAddress())
                .isDefault(document.isDefault())
                .build();
    }

    public static AddressDocument toDocument(AddressEntity entity) {
        if (entity == null) {
            return null;
        }

        return AddressDocument.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .receiverName(entity.getReceiverName())
                .receiverPhone(entity.getReceiverPhone())
                .provinceCode(entity.getProvinceCode())
                .districtCode(entity.getDistrictCode())
                .wardCode(entity.getWardCode())
                .provinceName(entity.getProvinceName())
                .districtName(entity.getDistrictName())
                .wardName(entity.getWardName())
                .streetAddress(entity.getStreetAddress())
                .isDefault(entity.isDefault())
                .build();
    }
}
