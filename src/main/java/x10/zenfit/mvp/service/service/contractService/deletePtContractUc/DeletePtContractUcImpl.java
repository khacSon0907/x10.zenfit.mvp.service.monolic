package x10.zenfit.mvp.service.service.contractService.deletePtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

@Component
@RequiredArgsConstructor
public class DeletePtContractUcImpl implements IDeletePtContractUc {

    private final IPtContractRepository ptContractRepository;

    @Override
    public void deletePtContract(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Contract ID là bắt buộc");
        }

        ptContractRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

        ptContractRepository.deleteById(id.trim());
    }
}
