package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import x10.zenfit.mvp.service.service.contractService.PtContractResponse;

public interface ICreatePtContractUc {
    PtContractResponse createPtContract(CreatePtContractRequest request);
}
