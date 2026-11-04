
package x10.zenfit.mvp.service.service.contractService.getAllPtContractsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetAllPtContractsUcImpl implements IGetAllPtContractsUc {

    private final IPtContractRepository ptContractRepository;

    @Override
    public List<PtContractEntity> getAllPtContracts(
            String trainerId,
            String clientId,
            ContractStatus status,
            PaymentContractStatus paymentContractStatus
    ) {

        // 1. Lấy tất cả hợp đồng
        List<PtContractEntity> contracts = ptContractRepository.findAll();

        // 2. Lọc theo huấn luyện viên
        if (trainerId != null && !trainerId.isBlank()) {
            String finalTrainerId = trainerId.trim();

            contracts = contracts.stream()
                    .filter(c -> Objects.equals(
                            c.getTrainerId(),
                            finalTrainerId
                    ))
                    .toList();
        }

        // 3. Lọc theo học viên
        if (clientId != null && !clientId.isBlank()) {
            String finalClientId = clientId.trim();

            contracts = contracts.stream()
                    .filter(c -> Objects.equals(
                            c.getClientId(),
                            finalClientId
                    ))
                    .toList();
        }

        // 4. Lọc theo trạng thái hợp đồng
        if (status != null) {
            contracts = contracts.stream()
                    .filter(c -> c.getStatus() == status)
                    .toList();
        }

        // 5. Lọc theo trạng thái thanh toán
        if (paymentContractStatus != null) {
            contracts = contracts.stream()
                    .filter(c -> c.getPaymentContractStatus() == paymentContractStatus)
                    .toList();
        }

        // 6. Trả về danh sách hợp đồng
        return contracts;
    }
}
