
package x10.zenfit.mvp.service.service.contractService.getPtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

@Component
@RequiredArgsConstructor
public class GetPtContractUcImpl implements IGetPtContractUc {

    private final IPtContractRepository ptContractRepository;

    @Override
    public PtContractEntity getPtContractById(String id) {

        // 1. Kiểm tra ID
        if (id == null || id.isBlank()) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 2. Tìm hợp đồng theo ID
        return ptContractRepository.findById(id.trim())
                .orElseThrow(() ->
                        new BusinessException(
                                PtContractError.CONTRACT_NOT_FOUND
                        )
                );
    }
}
