package x10.zenfit.mvp.service.infrastructure.contract.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;

import java.util.List;

@Repository
public interface PtContractMongoRepository extends MongoRepository<PtContractDocument, String> {

    List<PtContractDocument> findByClientId(String clientId);

    List<PtContractDocument> findByTrainerId(String trainerId);

    List<PtContractDocument> findByStatus(ContractStatus status);

    List<PtContractDocument> findByTrainerIdAndStatus(String trainerId, ContractStatus status);

    List<PtContractDocument> findByTrainerIdAndClientId(String trainerId, String clientId);
}
