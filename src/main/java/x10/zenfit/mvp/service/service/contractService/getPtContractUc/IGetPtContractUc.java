package x10.zenfit.mvp.service.service.contractService.getPtContractUc;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;

public interface IGetPtContractUc {
    PtContractEntity getPtContractById(String id);
}
