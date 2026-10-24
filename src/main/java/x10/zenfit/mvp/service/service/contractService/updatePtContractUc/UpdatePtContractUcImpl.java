package x10.zenfit.mvp.service.service.contractService.updatePtContractUc;

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

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdatePtContractUcImpl implements IUpdatePtContractUc {

    private final IPtContractRepository ptContractRepository;
    private final IClientRepository clientRepository;

    @Override
    public PtContractResponse updatePtContract(String id, UpdatePtContractRequest request) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Contract ID là bắt buộc");
        }
        if (request == null) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }

        PtContractEntity existing = ptContractRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

        if (request.getTrainerId() != null && !request.getTrainerId().isBlank()) {
            existing.setTrainerId(request.getTrainerId().trim());
        }

        if (request.getGoal() != null) {
            existing.setGoal(request.getGoal());
        }

        if (request.getTargetWeight() != null) {
            existing.setTargetWeight(request.getTargetWeight());
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

        if (request.getStartDate() != null) {
            existing.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            existing.setEndDate(request.getEndDate());
        }

        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }

        if (request.getPaidAmount() != null) {
            existing.setPaidAmount(request.getPaidAmount());
        }

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        if (request.getNotes() != null) {
            existing.setNotes(request.getNotes());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        PtContractEntity saved = ptContractRepository.save(existing);

        ClientEntity client = null;
        if (saved.getClientId() != null && !saved.getClientId().isBlank()) {
            client = clientRepository.findById(saved.getClientId()).orElse(null);
        }

        return PtContractResponseMapper.toResponse(saved, client);
    }
}
