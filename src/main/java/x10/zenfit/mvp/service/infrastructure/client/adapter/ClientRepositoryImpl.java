package x10.zenfit.mvp.service.infrastructure.client.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.infrastructure.client.adapter.mapper.ClientMapper;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientDocument;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
            String regex = ".*" + Pattern.quote(keyword.trim()) + ".*";
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
    public PaginatedResult<ClientEntity> findPtClientsWithPaging(
            String trainerId,
            String search,
            ClientStatus status,
            String sortBy,
            String sortDirection,
            int page,
            int size
    ) {
        int validPage = Math.max(page, 1);
        int validSize = size > 0 ? size : 8;

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // 1. Mandatory trainer isolation
        if (trainerId != null && !trainerId.isBlank()) {
            criteriaList.add(Criteria.where("assignedTrainerId").is(trainerId.trim()));
        }

        // 2. Search by name or phone (case-insensitive partial match)
        if (search != null && !search.isBlank()) {
            String regex = ".*" + Pattern.quote(search.trim()) + ".*";
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("fullName").regex(regex, "i"),
                    Criteria.where("phone").regex(regex, "i")
            );
            criteriaList.add(searchCriteria);
        }

        // 3. Filter by client status
        if (status != null) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // 4. Count total elements matching criteria
        long totalElements = mongoTemplate.count(query, ClientDocument.class);
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / validSize);
        long skip = (long) (validPage - 1) * validSize;

        // 5. Guard for empty result or skip exceeding total
        if (totalElements == 0 || skip >= totalElements) {
            return PaginatedResult.<ClientEntity>builder()
                    .items(List.of())
                    .page(validPage)
                    .size(validSize)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .hasNext(false)
                    .hasPrevious(validPage > 1 && totalPages > 0)
                    .build();
        }

        // 6. Sorting
        String validSortBy = "fullName".equalsIgnoreCase(sortBy) ? "fullName" : "createdAt";
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(direction, validSortBy));

        // 7. Pagination
        query.skip(skip).limit(validSize);

        List<ClientEntity> entities = mongoTemplate.find(query, ClientDocument.class)
                .stream()
                .map(ClientMapper::toEntity)
                .toList();

        return PaginatedResult.<ClientEntity>builder()
                .items(entities)
                .page(validPage)
                .size(validSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(validPage < totalPages)
                .hasPrevious(validPage > 1)
                .build();
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
