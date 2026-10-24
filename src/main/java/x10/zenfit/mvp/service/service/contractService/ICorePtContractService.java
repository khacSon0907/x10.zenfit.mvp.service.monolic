package x10.zenfit.mvp.service.service.contractService;

import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.CreatePtContractRequest;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.UpdatePtContractRequest;

import java.util.List;

public interface ICorePtContractService {

    PtContractResponse createPtContract(CreatePtContractRequest request);

    PtContractResponse getPtContractById(String id);

    List<PtContractResponse> getAllPtContracts(String trainerId, String clientId, ContractStatus status);

    PtContractResponse updatePtContract(String id, UpdatePtContractRequest request);

    void deletePtContract(String id);
}
