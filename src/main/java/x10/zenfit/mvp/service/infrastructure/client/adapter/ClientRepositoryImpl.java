package x10.zenfit.mvp.service.infrastructure.client.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.infrastructure.client.adapter.mapper.ClientMapper;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientDocument;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements IClientRepository {

    private final ClientMongoRepository clientMongoRepository;

    @Override
    public ClientEntity save(ClientEntity client) {
        ClientDocument document = ClientMapper.toDocument(client);
        ClientDocument saved = clientMongoRepository.save(document);
        return ClientMapper.toEntity(saved);
    }

    @Override
    public Optional<ClientEntity> findById(String id) {
        return clientMongoRepository.findById(id)
                .map(ClientMapper::toEntity);
    }

    @Override
    public Optional<ClientEntity> findByEmail(String email) {
        return clientMongoRepository.findByEmail(email)
                .map(ClientMapper::toEntity);
    }

    @Override
    public Optional<ClientEntity> findByPhone(String phone) {
        return clientMongoRepository.findByPhone(phone)
                .map(ClientMapper::toEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientMongoRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return clientMongoRepository.existsByPhone(phone);
    }

    @Override
    public List<ClientEntity> findAll() {
        return clientMongoRepository.findAll()
                .stream()
                .map(ClientMapper::toEntity)
                .toList();
    }

    @Override
    public List<ClientEntity> findByAssignedTrainerId(String assignedTrainerId) {
        return clientMongoRepository.findByAssignedTrainerId(assignedTrainerId)
                .stream()
                .map(ClientMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        clientMongoRepository.deleteById(id);
    }
}
