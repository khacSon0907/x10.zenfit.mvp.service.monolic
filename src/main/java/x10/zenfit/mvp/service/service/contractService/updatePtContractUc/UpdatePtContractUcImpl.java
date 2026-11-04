package x10.zenfit.mvp.service.service.contractService.updatePtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdatePtContractUcImpl implements IUpdatePtContractUc {

    private final IPtContractRepository ptContractRepository;

    @Override
    public PtContractEntity updatePtContract(String id, UpdatePtContractRequest request) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }
        if (request == null) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }

        PtContractEntity existing = ptContractRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

        // 1. Kiểm tra ngày hợp đồng
        LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : existing.getStartDate();
        LocalDate newEndDate = request.getEndDate() != null ? request.getEndDate() : existing.getEndDate();

        if (newStartDate != null && newEndDate != null && !newEndDate.isAfter(newStartDate)) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }

        if (request.getStartDate() != null) {
            existing.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            existing.setEndDate(request.getEndDate());
        }

        // 2. Kiểm tra các thông số buổi tập
        int totalSessions = request.getTotalSessions() != null ? request.getTotalSessions()
                : (existing.getTotalSessions() != null ? existing.getTotalSessions() : 0);
        int bonusSessions = request.getBonusSessions() != null ? request.getBonusSessions()
                : (existing.getBonusSessions() != null ? existing.getBonusSessions() : 0);
        int usedSessions = request.getUsedSessions() != null ? request.getUsedSessions()
                : (existing.getUsedSessions() != null ? existing.getUsedSessions() : 0);

        if (usedSessions > totalSessions + bonusSessions) {
            throw new BusinessException(PtContractError.SESSIONS_EXCEEDED);
        }

        if (request.getTotalSessions() != null) {
            existing.setTotalSessions(request.getTotalSessions());
        }
        if (request.getBonusSessions() != null) {
            existing.setBonusSessions(request.getBonusSessions());
        }
        if (request.getUsedSessions() != null) {
            existing.setUsedSessions(request.getUsedSessions());
        }

        // 3. Kiểm tra tài chính (giá & thanh toán)
        BigDecimal price = request.getPrice() != null ? request.getPrice() : existing.getPrice();
        BigDecimal paidAmount = request.getPaidAmount() != null ? request.getPaidAmount() : existing.getPaidAmount();

        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }
        if (paidAmount != null && paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }
        if (paidAmount != null && price != null && paidAmount.compareTo(price) > 0) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }

        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }
        if (request.getPaidAmount() != null) {
            existing.setPaidAmount(request.getPaidAmount());
        }

        // 4. Giá mỗi buổi tập
        if (request.getPricePerSession() != null) {
            existing.setPricePerSession(request.getPricePerSession());
        } else if ((request.getPrice() != null || request.getTotalSessions() != null)
                && price != null && totalSessions > 0) {
            existing.setPricePerSession(price.divide(BigDecimal.valueOf(totalSessions), 2, RoundingMode.HALF_UP));
        }

        // 5. Trạng thái thanh toán của hợp đồng
        if (request.getPaymentContractStatus() != null) {
            existing.setPaymentContractStatus(request.getPaymentContractStatus());
        } else if (request.getPaidAmount() != null || request.getPrice() != null) {
            if (paidAmount == null || paidAmount.compareTo(BigDecimal.ZERO) <= 0) {
                existing.setPaymentContractStatus(PaymentContractStatus.UNPAID);
            } else if (price != null && paidAmount.compareTo(price) >= 0) {
                existing.setPaymentContractStatus(PaymentContractStatus.PAID);
            } else {
                existing.setPaymentContractStatus(PaymentContractStatus.PARTIALLY_PAID);
            }
        }

        // 6. Trạng thái hợp đồng & ghi chú
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            existing.setNotes(request.getNotes());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return ptContractRepository.save(existing);
    }
}

