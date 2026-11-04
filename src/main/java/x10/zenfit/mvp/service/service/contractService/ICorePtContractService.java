package x10.zenfit.mvp.service.service.contractService;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.CreatePtContractRequest;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.UpdatePtContractRequest;

import java.util.List;

public interface ICorePtContractService {

    PtContractEntity createPtContract(CreatePtContractRequest request);

    PtContractEntity updatePtContract(String id, UpdatePtContractRequest request);

    PtContractEntity getPtContractById(String id);

    List<PtContractEntity> getAllPtContracts(
            String trainerId,
            String clientId,
            ContractStatus status,
            PaymentContractStatus paymentContractStatus
    );

    // Lấy tất cả hợp đồng theo trainerId
    List<PtContractEntity> getAllPtContractsByTrainerId(String trainerId);

    void deletePtContract(String id);
}
