package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;

import java.util.List;
import java.util.Optional;

public interface IAddressRepository {
    AddressEntity save(AddressEntity address);
    Optional<AddressEntity> findById(String id);
    List<AddressEntity> findByUserId(String userId);
    void deleteById(String id);
}
