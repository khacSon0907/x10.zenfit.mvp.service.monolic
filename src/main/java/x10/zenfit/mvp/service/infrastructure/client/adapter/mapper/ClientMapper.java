package x10.zenfit.mvp.service.infrastructure.client.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientDocument;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static ClientEntity toEntity(ClientDocument document) {
        if (document == null) {
            return null;
        }

        return ClientEntity.builder()
                .id(document.getId())
                .fullName(document.getFullName())
                .gender(document.getGender())
                .dateOfBirth(document.getDateOfBirth())
                .phone(document.getPhone())
                .email(document.getEmail())
                .address(document.getAddress())
                .avatarUrl(document.getAvatarUrl())
                .status(document.getStatus())
                .frontPhotoUrl(document.getFrontPhotoUrl())
                .backPhotoUrl(document.getBackPhotoUrl())
                .sidePhotoUrl(document.getSidePhotoUrl())
                .height(document.getHeight())
                .initialWeight(document.getInitialWeight())
                .assignedTrainerId(document.getAssignedTrainerId())
                .notes(document.getNotes())
                .joinedDate(document.getJoinedDate())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static ClientDocument toDocument(ClientEntity entity) {
        if (entity == null) {
            return null;
        }

        return ClientDocument.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .gender(entity.getGender())
                .dateOfBirth(entity.getDateOfBirth())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .avatarUrl(entity.getAvatarUrl())
                .status(entity.getStatus())
                .frontPhotoUrl(entity.getFrontPhotoUrl())
                .backPhotoUrl(entity.getBackPhotoUrl())
                .sidePhotoUrl(entity.getSidePhotoUrl())
                .height(entity.getHeight())
                .initialWeight(entity.getInitialWeight())
                .assignedTrainerId(entity.getAssignedTrainerId())
                .notes(entity.getNotes())
                .joinedDate(entity.getJoinedDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
