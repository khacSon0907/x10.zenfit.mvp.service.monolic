package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.guest.GuestEntity;

import java.util.Optional;

public interface IGuestRepository {

    GuestEntity save(GuestEntity guest);

    Optional<GuestEntity> findByEmail(String email);
}
