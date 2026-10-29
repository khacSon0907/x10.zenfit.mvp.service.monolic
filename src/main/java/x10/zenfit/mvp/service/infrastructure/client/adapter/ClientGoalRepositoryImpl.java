package x10.zenfit.mvp.service.infrastructure.client.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.infrastructure.client.adapter.mapper.ClientGoalMapper;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientGoalDocument;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientGoalMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientGoalRepositoryImpl implements IClientGoalRepository {

    private final ClientGoalMongoRepository clientGoalMongoRepository;

    @Override
    public ClientGoalEntity save(ClientGoalEntity goal) {
        ClientGoalDocument document = ClientGoalMapper.toDocument(goal);
        ClientGoalDocument saved = clientGoalMongoRepository.save(document);
        return ClientGoalMapper.toEntity(saved);
    }

    @Override
    public Optional<ClientGoalEntity> findById(String id) {
        return clientGoalMongoRepository.findById(id)
                .map(ClientGoalMapper::toEntity);
    }

    @Override
    public List<ClientGoalEntity> findByClientId(String clientId) {
        return clientGoalMongoRepository.findByClientId(clientId)
                .stream()
                .map(ClientGoalMapper::toEntity)
                .toList();
    }

    @Override
    public List<ClientGoalEntity> findByClientIdAndStatus(String clientId, GoalPhaseStatus status) {
        return clientGoalMongoRepository.findByClientIdAndStatus(clientId, status)
                .stream()
                .map(ClientGoalMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<ClientGoalEntity> findFirstByClientIdOrderByCreatedAtDesc(String clientId) {
        return clientGoalMongoRepository.findFirstByClientIdOrderByCreatedAtDesc(clientId)
                .map(ClientGoalMapper::toEntity);
    }

    @Override
    public Optional<ClientGoalEntity> findActiveGoalByClientId(String clientId) {
        return clientGoalMongoRepository.findFirstByClientIdAndStatusOrderByCreatedAtDesc(clientId, GoalPhaseStatus.ACTIVE)
                .map(ClientGoalMapper::toEntity);
    }

    @Override
    public void deleteById(String id) {
        clientGoalMongoRepository.deleteById(id);
    }
}
