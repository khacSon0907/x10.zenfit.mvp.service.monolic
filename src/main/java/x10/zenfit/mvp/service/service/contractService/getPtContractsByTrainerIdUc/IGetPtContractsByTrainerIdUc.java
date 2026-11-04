package x10.zenfit.mvp.service.service.contractService.getPtContractsByTrainerIdUc;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;

import java.util.List;

public interface IGetPtContractsByTrainerIdUc {

    List<PtContractEntity> execute(String trainerId);

}