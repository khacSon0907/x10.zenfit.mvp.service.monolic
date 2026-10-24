package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;
import x10.zenfit.mvp.service.service.contractService.PtContractResponse;
import x10.zenfit.mvp.service.service.contractService.PtContractResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreatePtContractUcImpl implements ICreatePtContractUc {

    private final IPtContractRepository ptContractRepository;
    private final IClientRepository clientRepository;

    @Override
    public PtContractResponse createPtContract(CreatePtContractRequest request) {
        if (request == null) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }

        // 1. Phân giải hoặc tự động tạo mới ClientEntity nếu chưa có
        ClientEntity resolvedClient = resolveOrCreateClient(request);

        // 2. Thiết lập các thông số mặc định cho hợp đồng
        LocalDateTime now = LocalDateTime.now();
        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        Integer bonusSessions = request.getBonusSessions() != null ? request.getBonusSessions() : 0;
        BigDecimal paidAmount = request.getPaidAmount() != null ? request.getPaidAmount() : BigDecimal.ZERO;
        ContractStatus status = request.getStatus() != null ? request.getStatus() : ContractStatus.ACTIVE;

        PtContractEntity contract = PtContractEntity.builder()
                .clientId(resolvedClient.getId())
                .trainerId(request.getTrainerId().trim())
                .goal(request.getGoal())
                .targetWeight(request.getTargetWeight() != null ? request.getTargetWeight() : resolvedClient.getTargetWeight())
                .totalSessions(request.getTotalSessions())
                .bonusSessions(bonusSessions)
                .usedSessions(0)
                .startDate(startDate)
                .endDate(request.getEndDate())
                .price(request.getPrice())
                .paidAmount(paidAmount)
                .status(status)
                .notes(request.getNotes())
                .createdAt(now)
                .updatedAt(now)
                .build();

        PtContractEntity saved = ptContractRepository.save(contract);
        return PtContractResponseMapper.toResponse(saved, resolvedClient);
    }

    private ClientEntity resolveOrCreateClient(CreatePtContractRequest request) {
        // Trường hợp 1: Có truyền clientId cụ thể
        if (request.getClientId() != null && !request.getClientId().isBlank()) {
            Optional<ClientEntity> clientOpt = clientRepository.findById(request.getClientId().trim());
            if (clientOpt.isPresent()) {
                return clientOpt.get();
            }
            // Nếu clientId truyền vào nhưng không tìm thấy và không có thông tin bổ sung -> lỗi
            if (request.getClientInfo() == null && (request.getClientPhone() == null || request.getClientPhone().isBlank())) {
                throw new BusinessException(ClientError.CLIENT_NOT_FOUND);
            }
        }

        // Lấy thông tin khách hàng từ clientInfo lồng hoặc từ các trường phẳng
        CreateClientRequest clientInfo = request.getClientInfo();
        String fullName = clientInfo != null ? clientInfo.getFullName() : request.getClientFullName();
        String phone = clientInfo != null ? clientInfo.getPhone() : request.getClientPhone();
        String email = clientInfo != null ? clientInfo.getEmail() : request.getClientEmail();
        String gender = clientInfo != null ? clientInfo.getGender() : request.getClientGender();
        String address = clientInfo != null ? clientInfo.getAddress() : request.getClientAddress();

        // Kiểm tra xem đã có số điện thoại để tra cứu/tạo mới chưa
        if ((phone == null || phone.isBlank()) && (email == null || email.isBlank()) && (fullName == null || fullName.isBlank())) {
            throw new BusinessException(PtContractError.CLIENT_INFO_REQUIRED);
        }

        // Trường hợp 2: Kiểm tra khách hàng đã tồn tại qua số điện thoại chưa
        if (phone != null && !phone.isBlank()) {
            Optional<ClientEntity> byPhone = clientRepository.findByPhone(phone.trim());
            if (byPhone.isPresent()) {
                return byPhone.get();
            }
        }

        // Kiểm tra khách hàng đã tồn tại qua email chưa
        if (email != null && !email.isBlank()) {
            Optional<ClientEntity> byEmail = clientRepository.findByEmail(email.trim().toLowerCase());
            if (byEmail.isPresent()) {
                return byEmail.get();
            }
        }

        // Trường hợp 3: Khách hàng chưa có trong hệ thống -> Tạo mới ClientEntity
        if (fullName == null || fullName.isBlank()) {
            fullName = "Khách hàng " + (phone != null ? phone.trim() : "");
        }

        LocalDateTime now = LocalDateTime.now();
        ClientEntity newClient = ClientEntity.builder()
                .fullName(fullName.trim())
                .gender(gender)
                .dateOfBirth(clientInfo != null ? clientInfo.getDateOfBirth() : null)
                .phone(phone != null && !phone.isBlank() ? phone.trim() : null)
                .email(email != null && !email.isBlank() ? email.trim().toLowerCase() : null)
                .address(address)
                .avatarUrl(clientInfo != null ? clientInfo.getAvatarUrl() : null)
                .status(clientInfo != null && clientInfo.getStatus() != null ? clientInfo.getStatus() : ClientStatus.ACTIVE)
                .height(clientInfo != null ? clientInfo.getHeight() : null)
                .initialWeight(clientInfo != null ? clientInfo.getInitialWeight() : null)
                .fitnessGoal(clientInfo != null ? clientInfo.getFitnessGoal() : null)
                .experienceLevel(clientInfo != null ? clientInfo.getExperienceLevel() : null)
                .targetWeight(clientInfo != null ? clientInfo.getTargetWeight() : request.getTargetWeight())
                .medicalConditions(clientInfo != null ? clientInfo.getMedicalConditions() : null)
                .injuries(clientInfo != null ? clientInfo.getInjuries() : null)
                .emergencyContactName(clientInfo != null ? clientInfo.getEmergencyContactName() : null)
                .emergencyContactPhone(clientInfo != null ? clientInfo.getEmergencyContactPhone() : null)
                .assignedTrainerId(request.getTrainerId().trim())
                .notes(clientInfo != null ? clientInfo.getNotes() : null)
                .joinedDate(clientInfo != null && clientInfo.getJoinedDate() != null ? clientInfo.getJoinedDate() : LocalDate.now())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return clientRepository.save(newClient);
    }
}
