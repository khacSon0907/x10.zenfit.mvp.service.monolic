package x10.zenfit.mvp.service.service.contractService.getAllPtContractsUc;

import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.contractService.PtContractResponse;

import java.util.List;

public interface IGetAllPtContractsUc {
    List<PtContractResponse> getAllPtContracts(String trainerId, String clientId, ContractStatus status);
}
