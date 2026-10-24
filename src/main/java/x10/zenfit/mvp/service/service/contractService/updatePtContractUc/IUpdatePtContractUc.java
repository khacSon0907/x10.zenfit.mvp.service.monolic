package x10.zenfit.mvp.service.service.contractService.updatePtContractUc;

import x10.zenfit.mvp.service.service.contractService.PtContractResponse;

public interface IUpdatePtContractUc {
    PtContractResponse updatePtContract(String id, UpdatePtContractRequest request);
}
