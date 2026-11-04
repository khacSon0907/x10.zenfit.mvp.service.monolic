 
package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;

import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreatePtContractUcImpl implements ICreatePtContractUc {

    private final IPtContractRepository ptContractRepository;
    private final IClientRepository clientRepository;

    @Override
    public PtContractEntity createPtContract(CreatePtContractRequest request) {

        // 1. Kiểm tra request
        if (request == null) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 2. Kiểm tra học viên
        if (request.getClientId() == null
                || request.getClientId().isBlank()) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        Optional<ClientEntity> clientOpt =
                clientRepository.findById(request.getClientId().trim());

        if (clientOpt.isEmpty()) {
            throw new BusinessException(ClientError.CLIENT_NOT_FOUND);
        }

        ClientEntity client = clientOpt.get();

        // 3. Kiểm tra huấn luyện viên
        if (request.getTrainerId() == null
                || request.getTrainerId().isBlank()) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 4. Kiểm tra tổng số buổi
        if (request.getTotalSessions() == null
                || request.getTotalSessions() < 1) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 5. Kiểm tra số buổi tặng
        Integer bonusSessions = request.getBonusSessions() != null
                ? request.getBonusSessions() : 0;

        if (bonusSessions < 0) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 6. Kiểm tra ngày hợp đồng
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (startDate == null || endDate == null
                || !endDate.isAfter(startDate)) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 7. Kiểm tra giá trị hợp đồng
        BigDecimal price = request.getPrice();

        if (price == null
                || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // 8. Kiểm tra số tiền đã thanh toán
        BigDecimal paidAmount = request.getPaidAmount() != null
                ? request.getPaidAmount() : BigDecimal.ZERO;

        if (paidAmount.compareTo(BigDecimal.ZERO) < 0
                || paidAmount.compareTo(price) > 0) {
            throw new BusinessException(
                    PtContractError.INVALID_CONTRACT_DATA
            );
        }

        // Tính hoặc lấy giá tiền một buổi tập
        BigDecimal pricePerSession = request.getPricePerSession();
        if (pricePerSession == null && request.getTotalSessions() != null && request.getTotalSessions() > 0) {
            pricePerSession = price.divide(BigDecimal.valueOf(request.getTotalSessions()), 2, RoundingMode.HALF_UP);
        }

        // Xác định trạng thái thanh toán của hợp đồng
        PaymentContractStatus paymentContractStatus = request.getPaymentContractStatus();
        if (paymentContractStatus == null) {
            if (paidAmount.compareTo(BigDecimal.ZERO) <= 0) {
                paymentContractStatus = PaymentContractStatus.UNPAID;
            } else if (paidAmount.compareTo(price) >= 0) {
                paymentContractStatus = PaymentContractStatus.PAID;
            } else {
                paymentContractStatus = PaymentContractStatus.PARTIALLY_PAID;
            }
        }

        // 9. Khởi tạo hợp đồng
        LocalDateTime now = LocalDateTime.now();

        PtContractEntity contract = PtContractEntity.builder()
                .clientId(client.getId())
                .trainerId(request.getTrainerId().trim())

                .totalSessions(request.getTotalSessions())
                .bonusSessions(bonusSessions)
                .usedSessions(0)

                .startDate(startDate)
                .endDate(endDate)

                .price(price)
                .pricePerSession(pricePerSession)
                .paidAmount(paidAmount)

                .status(ContractStatus.ACTIVE)
                .paymentContractStatus(paymentContractStatus)
                .notes(request.getNotes())

                .createdAt(now)
                .updatedAt(now)
                .build();

        // 10. Lưu hợp đồng
        PtContractEntity saved =
                ptContractRepository.save(contract);

        // 11. Trả về hợp đồng đã lưu
        return saved;
    }
}
