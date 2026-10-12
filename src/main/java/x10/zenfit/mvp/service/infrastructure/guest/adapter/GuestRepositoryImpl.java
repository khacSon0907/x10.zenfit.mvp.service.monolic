package x10.zenfit.mvp.service.infrastructure.guest.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IGuestRepository;
import x10.zenfit.mvp.service.common.domain.entities.guest.GuestEntity;
import x10.zenfit.mvp.service.infrastructure.guest.db.mongodb.GuestDocument;
import x10.zenfit.mvp.service.infrastructure.guest.db.mongodb.GuestMongoRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GuestRepositoryImpl implements IGuestRepository {

    private final GuestMongoRepository guestMongoRepository;

    @Override
    public GuestEntity save(GuestEntity guest) {
        GuestDocument document = GuestMapper.toDocument(guest);
        GuestDocument saved = guestMongoRepository.save(document);
        return GuestMapper.toEntity(saved);
    }

    @Override
    public Optional<GuestEntity> findByEmail(String email) {
        return guestMongoRepository.findByEmail(email)
                .map(GuestMapper::toEntity);
    }
}
