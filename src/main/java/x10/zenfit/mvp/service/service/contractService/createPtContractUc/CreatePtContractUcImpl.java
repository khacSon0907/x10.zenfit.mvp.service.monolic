package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;
import x10.zenfit.mvp.service.service.contractService.PtContractResponse;
import x10.zenfit.mvp.service.service.contractService.PtContractResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreatePtContractUcImpl implements ICreatePtContractUc {

    private final IPtContractRepository ptContractRepository;
    private final IClientRepository clientRepository;
    private final IClientGoalRepository clientGoalRepository;

    @Override
    public PtContractResponse createPtContract(CreatePtContractRequest request) {
        if (request == null) {
            throw new BusinessException(PtContractError.INVALID_CONTRACT_DATA);
        }

        // 1. Phân giải khách hàng: Đã có sẵn (chọn qua clientId hoặc tìm theo tên/SĐT) hoặc tự động tạo mới
        ClientEntity resolvedClient = resolveOrCreateClient(request);

        // 2. Thiết lập mục tiêu khách hàng (ClientGoalEntity) nếu có thông tin mục tiêu
        ClientGoalEntity savedGoal = resolveOrCreateGoal(request, resolvedClient);

        // 3. Khởi tạo và lưu hợp đồng PT (PtContractEntity)
        LocalDateTime now = LocalDateTime.now();
        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        Integer bonusSessions = request.getBonusSessions() != null ? request.getBonusSessions() : 0;
        BigDecimal paidAmount = request.getPaidAmount() != null ? request.getPaidAmount() : BigDecimal.ZERO;
        ContractStatus status = request.getStatus() != null ? request.getStatus() : ContractStatus.ACTIVE;

        PtContractEntity contract = PtContractEntity.builder()
                .clientId(resolvedClient.getId())
                .trainerId(request.getTrainerId().trim())
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
        return PtContractResponseMapper.toResponse(saved, resolvedClient, savedGoal);
    }

    private ClientEntity resolveOrCreateClient(CreatePtContractRequest request) {
        // Trường hợp 1: Có truyền clientId cụ thể (đã chọn từ dropdown hoặc kết quả tìm kiếm)
        if (request.getClientId() != null && !request.getClientId().isBlank()) {
            Optional<ClientEntity> clientOpt = clientRepository.findById(request.getClientId().trim());
            if (clientOpt.isPresent()) {
                return clientOpt.get();
            }
            // Nếu clientId truyền vào nhưng không tìm thấy và không có thông tin tạo mới -> báo lỗi
            if (request.getClientInfo() == null && (request.getClientFullName() == null || request.getClientFullName().isBlank())) {
                throw new BusinessException(ClientError.CLIENT_NOT_FOUND);
            }
        }

        // Lấy thông tin khách hàng từ object clientInfo lồng hoặc từ các trường phẳng
        CreateClientRequest clientInfo = request.getClientInfo();
        String fullName = clientInfo != null ? clientInfo.getFullName() : request.getClientFullName();
        if (fullName == null || fullName.isBlank()) {
            fullName = request.getSearchClientName();
        }

        String phone = clientInfo != null ? clientInfo.getPhone() : request.getClientPhone();
        String email = clientInfo != null ? clientInfo.getEmail() : request.getClientEmail();
        String gender = clientInfo != null ? clientInfo.getGender() : request.getClientGender();
        LocalDate dob = clientInfo != null ? clientInfo.getDateOfBirth() : request.getClientDateOfBirth();
        String address = clientInfo != null ? clientInfo.getAddress() : request.getClientAddress();
        String avatarUrl = clientInfo != null ? clientInfo.getAvatarUrl() : request.getClientAvatarUrl();

        String frontPhotoUrl = clientInfo != null ? clientInfo.getFrontPhotoUrl() : request.getClientFrontPhotoUrl();
        String backPhotoUrl = clientInfo != null ? clientInfo.getBackPhotoUrl() : request.getClientBackPhotoUrl();
        String sidePhotoUrl = clientInfo != null ? clientInfo.getSidePhotoUrl() : request.getClientSidePhotoUrl();

        Double height = clientInfo != null ? clientInfo.getHeight() : request.getHeight();
        Double initialWeight = clientInfo != null ? clientInfo.getInitialWeight() : request.getInitialWeight();
        String clientNotes = clientInfo != null ? clientInfo.getNotes() : request.getClientNotes();

        // Trường hợp 2: Kiểm tra xem đã có khách hàng theo Số điện thoại chưa
        if (phone != null && !phone.isBlank()) {
            Optional<ClientEntity> byPhone = clientRepository.findByPhone(phone.trim());
            if (byPhone.isPresent()) {
                return byPhone.get();
            }
        }

        // Trường hợp 3: Kiểm tra theo tên tìm kiếm nếu PT nhập tên người đó
        if (fullName != null && !fullName.isBlank()) {
            List<ClientEntity> matchedClients = clientRepository.searchClients(fullName.trim(), request.getTrainerId().trim());
            if (matchedClients.size() == 1) {
                return matchedClients.getFirst();
            } else if (matchedClients.isEmpty()) {
                // Thử tìm theo họ tên chính xác không phân biệt hoa thường trong toàn bộ khách hàng
                List<ClientEntity> byExactName = clientRepository.findByFullName(fullName.trim());
                if (byExactName.size() == 1) {
                    return byExactName.getFirst();
                }
            }
        }

        // Trường hợp 4: Khách hàng chưa được tạo trước đó -> Tự động tạo mới ClientEntity
        if ((fullName == null || fullName.isBlank()) && (phone == null || phone.isBlank())) {
            throw new BusinessException(PtContractError.CLIENT_INFO_REQUIRED);
        }

        if (fullName == null || fullName.isBlank()) {
            fullName = "Khách hàng " + (phone != null ? phone.trim() : "");
        }

        LocalDateTime now = LocalDateTime.now();
        ClientEntity newClient = ClientEntity.builder()
                .fullName(fullName.trim())
                .gender(gender)
                .dateOfBirth(dob)
                .phone(phone != null && !phone.isBlank() ? phone.trim() : null)
                .email(email != null && !email.isBlank() ? email.trim().toLowerCase() : null)
                .address(address)
                .avatarUrl(avatarUrl)
                .status(clientInfo != null && clientInfo.getStatus() != null ? clientInfo.getStatus() : ClientStatus.ACTIVE)
                .frontPhotoUrl(frontPhotoUrl)
                .backPhotoUrl(backPhotoUrl)
                .sidePhotoUrl(sidePhotoUrl)
                .height(height)
                .initialWeight(initialWeight)
                .assignedTrainerId(request.getTrainerId().trim())
                .notes(clientNotes)
                .joinedDate(clientInfo != null && clientInfo.getJoinedDate() != null ? clientInfo.getJoinedDate() : LocalDate.now())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return clientRepository.save(newClient);
    }

    private ClientGoalEntity resolveOrCreateGoal(CreatePtContractRequest request, ClientEntity client) {
        boolean hasGoalInfo = (request.getGoalType() != null && !request.getGoalType().isBlank())
                || request.getTargetWeight() != null
                || (request.getGoalNote() != null && !request.getGoalNote().isBlank());

        if (!hasGoalInfo) {
            // Nếu không truyền mục tiêu mới, thử lấy mục tiêu đang hoạt động của khách hàng này
            return clientGoalRepository.findActiveGoalByClientId(client.getId()).orElse(null);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDate goalStartDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate goalEndDate = request.getEndDate();
        Double startWeight = request.getStartWeight() != null ? request.getStartWeight() : client.getInitialWeight();

        ClientGoalEntity goal = ClientGoalEntity.builder()
                .clientId(client.getId())
                .goalType(request.getGoalType())
                .startDate(goalStartDate)
                .endDate(goalEndDate)
                .startWeight(startWeight)
                .targetWeight(request.getTargetWeight())
                .status(GoalPhaseStatus.ACTIVE)
                .note(request.getGoalNote())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return clientGoalRepository.save(goal);
    }
}
