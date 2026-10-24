package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;

import java.util.List;
import java.util.Optional;

public interface IPtContractRepository {

    PtContractEntity save(PtContractEntity contract);

    Optional<PtContractEntity> findById(String id);

    List<PtContractEntity> findAll();

    List<PtContractEntity> findByClientId(String clientId);

    List<PtContractEntity> findByTrainerId(String trainerId);

    List<PtContractEntity> findByStatus(ContractStatus status);

    List<PtContractEntity> findByTrainerIdAndStatus(String trainerId, ContractStatus status);

    List<PtContractEntity> findByTrainerIdAndClientId(String trainerId, String clientId);

    void deleteById(String id);

    boolean existsById(String id);
}
