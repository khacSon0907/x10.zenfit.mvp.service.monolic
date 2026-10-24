package x10.zenfit.mvp.service.service.contractService.getPtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.service.contractService.PtContractResponse;
import x10.zenfit.mvp.service.service.contractService.PtContractResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

@Component
@RequiredArgsConstructor
public class GetPtContractUcImpl implements IGetPtContractUc {

    private final IPtContractRepository ptContractRepository;
    private final IClientRepository clientRepository;

    @Override
    public PtContractResponse getPtContractById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Contract ID là bắt buộc");
        }

        PtContractEntity contract = ptContractRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

        ClientEntity client = null;
        if (contract.getClientId() != null && !contract.getClientId().isBlank()) {
            client = clientRepository.findById(contract.getClientId()).orElse(null);
        }

        return PtContractResponseMapper.toResponse(contract, client);
    }
}
