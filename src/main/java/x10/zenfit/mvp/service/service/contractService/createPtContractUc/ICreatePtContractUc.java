package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;

public interface ICreatePtContractUc {
    PtContractEntity createPtContract(CreatePtContractRequest request);
}
