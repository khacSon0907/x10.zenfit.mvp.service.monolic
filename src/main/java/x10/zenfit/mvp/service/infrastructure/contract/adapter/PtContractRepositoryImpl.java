package x10.zenfit.mvp.service.infrastructure.contract.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.infrastructure.contract.adapter.mapper.PtContractMapper;
import x10.zenfit.mvp.service.infrastructure.contract.db.mongodb.PtContractDocument;
import x10.zenfit.mvp.service.infrastructure.contract.db.mongodb.PtContractMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PtContractRepositoryImpl implements IPtContractRepository {

    private final PtContractMongoRepository mongoRepository;

    @Override
    public PtContractEntity save(PtContractEntity contract) {
        PtContractDocument document = PtContractMapper.toDocument(contract);
        PtContractDocument saved = mongoRepository.save(document);
        return PtContractMapper.toEntity(saved);
    }

    @Override
    public Optional<PtContractEntity> findById(String id) {
        return mongoRepository.findById(id)
                .map(PtContractMapper::toEntity);
    }

    @Override
    public List<PtContractEntity> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(PtContractMapper::toEntity)
                .toList();
    }

    @Override
    public List<PtContractEntity> findByClientId(String clientId) {
        return mongoRepository.findByClientId(clientId)
                .stream()
                .map(PtContractMapper::toEntity)
                .toList();
    }

    @Override
    public List<PtContractEntity> findByTrainerId(String trainerId) {
        return mongoRepository.findByTrainerId(trainerId)
                .stream()
                .map(PtContractMapper::toEntity)
                .toList();
    }

    @Override
    public List<PtContractEntity> findByStatus(ContractStatus status) {
        return mongoRepository.findByStatus(status)
                .stream()
                .map(PtContractMapper::toEntity)
                .toList();
    }

    @Override
    public List<PtContractEntity> findByTrainerIdAndStatus(String trainerId, ContractStatus status) {
        return mongoRepository.findByTrainerIdAndStatus(trainerId, status)
                .stream()
                .map(PtContractMapper::toEntity)
                .toList();
    }

    @Override
    public List<PtContractEntity> findByTrainerIdAndClientId(String trainerId, String clientId) {
        return mongoRepository.findByTrainerIdAndClientId(trainerId, clientId)
                .stream()
                .map(PtContractMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }
}
