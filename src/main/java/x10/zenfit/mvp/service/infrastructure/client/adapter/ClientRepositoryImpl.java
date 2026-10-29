package x10.zenfit.mvp.service.infrastructure.client.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.infrastructure.client.adapter.mapper.ClientMapper;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientDocument;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements IClientRepository {

    private final ClientMongoRepository clientMongoRepository;
    private final MongoTemplate mongoTemplate;

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
    public List<ClientEntity> searchClients(String keyword, String trainerId) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            String regex = ".*" + keyword.trim() + ".*";
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("fullName").regex(regex, "i"),
                    Criteria.where("phone").regex(regex, "i"),
                    Criteria.where("email").regex(regex, "i")
            );
            criteriaList.add(searchCriteria);
        }

        if (trainerId != null && !trainerId.isBlank()) {
            criteriaList.add(Criteria.where("assignedTrainerId").is(trainerId.trim()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, ClientDocument.class)
                .stream()
                .map(ClientMapper::toEntity)
                .toList();
    }

    @Override
    public List<ClientEntity> findByFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return List.of();
        }
        return clientMongoRepository.findByFullNameIgnoreCase(fullName.trim())
                .stream()
                .map(ClientMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        clientMongoRepository.deleteById(id);
    }
}
