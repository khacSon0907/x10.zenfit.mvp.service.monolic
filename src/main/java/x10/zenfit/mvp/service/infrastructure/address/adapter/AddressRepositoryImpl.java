package x10.zenfit.mvp.service.infrastructure.address.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IAddressRepository;
import x10.zenfit.mvp.service.common.domain.entities.address.AddressEntity;
import x10.zenfit.mvp.service.infrastructure.address.adapter.mapper.AddressMapper;
import x10.zenfit.mvp.service.infrastructure.address.db.mongodb.AddressDocument;
import x10.zenfit.mvp.service.infrastructure.address.db.mongodb.AddressMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements IAddressRepository {

    private final AddressMongoRepository addressMongoRepository;

    @Override
    public AddressEntity save(AddressEntity address) {
        AddressDocument document = AddressMapper.toDocument(address);
        AddressDocument savedDocument = addressMongoRepository.save(document);
        return AddressMapper.toEntity(savedDocument);
    }

    @Override
    public Optional<AddressEntity> findById(String id) {
        return addressMongoRepository.findById(id)
                .map(AddressMapper::toEntity);
    }

    @Override
    public List<AddressEntity> findByUserId(String userId) {
        return addressMongoRepository.findByUserId(userId)
                .stream()
                .map(AddressMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        addressMongoRepository.deleteById(id);
    }
}
