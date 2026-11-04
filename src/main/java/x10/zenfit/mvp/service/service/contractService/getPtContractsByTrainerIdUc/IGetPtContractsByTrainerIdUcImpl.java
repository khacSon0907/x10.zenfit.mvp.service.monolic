package x10.zenfit.mvp.service.service.contractService.getPtContractsByTrainerIdUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.service.contractService.ICorePtContractService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IGetPtContractsByTrainerIdUcImpl implements IGetPtContractsByTrainerIdUc {

    private final IPtContractRepository ptContractRepository;

    @Override
    public List<PtContractEntity> execute(String trainerId) {
        return ptContractRepository.findByTrainerId(trainerId);
    }
}