package x10.zenfit.mvp.service.service.contractService.updatePtContractUc;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;

public interface IUpdatePtContractUc {
    PtContractEntity updatePtContract(String id, UpdatePtContractRequest request);
}

