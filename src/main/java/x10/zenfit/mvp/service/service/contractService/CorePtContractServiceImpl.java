package x10.zenfit.mvp.service.service.contractService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.CreatePtContractRequest;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.ICreatePtContractUc;
import x10.zenfit.mvp.service.service.contractService.deletePtContractUc.IDeletePtContractUc;
import x10.zenfit.mvp.service.service.contractService.getAllPtContractsUc.IGetAllPtContractsUc;
import x10.zenfit.mvp.service.service.contractService.getPtContractUc.IGetPtContractUc;
import x10.zenfit.mvp.service.service.contractService.getPtContractsByTrainerIdUc.IGetPtContractsByTrainerIdUc;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.IUpdatePtContractUc;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.UpdatePtContractRequest;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CorePtContractServiceImpl implements ICorePtContractService {

    private final ICreatePtContractUc createPtContractUc;
    private final IUpdatePtContractUc updatePtContractUc;
    private final IGetPtContractUc getPtContractUc;
    private final IGetAllPtContractsUc getAllPtContractsUc;
    private final IDeletePtContractUc deletePtContractUc;
    private final IGetPtContractsByTrainerIdUc getPtContractsByTrainerIdUc;

    @Override
    public List<PtContractEntity> getAllPtContractsByTrainerId(String trainerId) {
        return getPtContractsByTrainerIdUc.execute(trainerId);
    }

    @Override
    public PtContractEntity createPtContract(CreatePtContractRequest request) {
        return createPtContractUc.createPtContract(request);
    }

    @Override
    public PtContractEntity updatePtContract(String id, UpdatePtContractRequest request) {
        return updatePtContractUc.updatePtContract(id, request);
    }

    @Override
    public PtContractEntity getPtContractById(String id) {
        return getPtContractUc.getPtContractById(id);
    }

    @Override
    public List<PtContractEntity> getAllPtContracts(
            String trainerId,
            String clientId,
            ContractStatus status,
            PaymentContractStatus paymentContractStatus
    ) {
        return getAllPtContractsUc.getAllPtContracts(trainerId, clientId, status, paymentContractStatus);
    }



    @Override
    public void deletePtContract(String id) {
        deletePtContractUc.deletePtContract(id);
    }
}
