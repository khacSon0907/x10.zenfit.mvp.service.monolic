package x10.zenfit.mvp.service.service.contractService.getAllPtContractsUc;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;

import java.util.List;

public interface IGetAllPtContractsUc {
    List<PtContractEntity> getAllPtContracts(
            String trainerId,
            String clientId,
            ContractStatus status,
            PaymentContractStatus paymentContractStatus
    );
}
