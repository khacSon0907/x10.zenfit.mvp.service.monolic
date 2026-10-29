package x10.zenfit.mvp.service.service.clientService;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;

public final class ClientResponseMapper {

    private ClientResponseMapper() {
    }

    public static ClientResponse toResponse(ClientEntity entity) {
        if (entity == null) {
            return null;
        }

        return ClientResponse.builder()
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
