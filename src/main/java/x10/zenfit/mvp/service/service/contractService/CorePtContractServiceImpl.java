package x10.zenfit.mvp.service.service.contractService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.CreatePtContractRequest;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.ICreatePtContractUc;
import x10.zenfit.mvp.service.service.contractService.deletePtContractUc.IDeletePtContractUc;
import x10.zenfit.mvp.service.service.contractService.getAllPtContractsUc.IGetAllPtContractsUc;
import x10.zenfit.mvp.service.service.contractService.getPtContractUc.IGetPtContractUc;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.IUpdatePtContractUc;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.UpdatePtContractRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CorePtContractServiceImpl implements ICorePtContractService {

    private final ICreatePtContractUc createPtContractUc;
    private final IGetPtContractUc getPtContractUc;
    private final IGetAllPtContractsUc getAllPtContractsUc;
    private final IUpdatePtContractUc updatePtContractUc;
    private final IDeletePtContractUc deletePtContractUc;

    @Override
    public PtContractResponse createPtContract(CreatePtContractRequest request) {
        return createPtContractUc.createPtContract(request);
    }

    @Override
    public PtContractResponse getPtContractById(String id) {
        return getPtContractUc.getPtContractById(id);
    }

    @Override
    public List<PtContractResponse> getAllPtContracts(String trainerId, String clientId, ContractStatus status) {
        return getAllPtContractsUc.getAllPtContracts(trainerId, clientId, status);
    }

    @Override
    public PtContractResponse updatePtContract(String id, UpdatePtContractRequest request) {
        return updatePtContractUc.updatePtContract(id, request);
    }

    @Override
    public void deletePtContract(String id) {
        deletePtContractUc.deletePtContract(id);
    }
}
