package x10.zenfit.mvp.service.service.clientService.createClientUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateClientUcImpl implements ICreateClientUc {

    private final IClientRepository clientRepository;

    @Override
    public ClientResponse createClient(CreateClientRequest request) {
        if (request == null) {
            throw new BusinessException(ClientError.INVALID_CLIENT_DATA);
        }

        // Kiểm tra số điện thoại trùng lặp
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            if (clientRepository.existsByPhone(request.getPhone().trim())) {
                throw new BusinessException(ClientError.CLIENT_PHONE_ALREADY_EXISTS);
            }
        }

        // Kiểm tra email trùng lặp (nếu có cung cấp)
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (clientRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
                throw new BusinessException(ClientError.CLIENT_EMAIL_ALREADY_EXISTS);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        ClientEntity entity = ClientEntity.builder()
                .fullName(request.getFullName().trim())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .phone(request.getPhone() != null ? request.getPhone().trim() : null)
                .email(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null)
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl())
                .status(request.getStatus() != null ? request.getStatus() : ClientStatus.ACTIVE)
                .height(request.getHeight())
                .initialWeight(request.getInitialWeight())
                .fitnessGoal(request.getFitnessGoal())
                .experienceLevel(request.getExperienceLevel())
                .targetWeight(request.getTargetWeight())
                .medicalConditions(request.getMedicalConditions())
                .injuries(request.getInjuries())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .assignedTrainerId(request.getAssignedTrainerId())
                .notes(request.getNotes())
                .joinedDate(request.getJoinedDate() != null ? request.getJoinedDate() : LocalDate.now())
                .createdAt(now)
                .updatedAt(now)
                .build();

        ClientEntity saved = clientRepository.save(entity);
        return ClientResponseMapper.toResponse(saved);
    }
}
