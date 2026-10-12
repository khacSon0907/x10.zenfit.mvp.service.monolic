package x10.zenfit.mvp.service.infrastructure.guest.adapter;

import x10.zenfit.mvp.service.common.domain.entities.guest.GuestEntity;
import x10.zenfit.mvp.service.infrastructure.guest.db.mongodb.GuestDocument;

public class GuestMapper {

    public static GuestEntity toEntity(GuestDocument document) {
        if (document == null) {
            return null;
        }

        return GuestEntity.builder()
                .id(document.getId())
                .email(document.getEmail())
                .username(document.getUsername())
                .createdAt(document.getCreatedAt())
                .build();
    }

    public static GuestDocument toDocument(GuestEntity entity) {
        if (entity == null) {
            return null;
        }

        return GuestDocument.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
