package x10.zenfit.mvp.service.service.clientService.updateClientUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdateClientUcImpl implements IUpdateClientUc {

    private final IClientRepository clientRepository;

    @Override
    public ClientResponse updateClient(String id, UpdateClientRequest request) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Client ID là bắt buộc");
        }
        if (request == null) {
            throw new BusinessException(ClientError.INVALID_CLIENT_DATA);
        }

        ClientEntity existing = clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        // Kiểm tra số điện thoại nếu thay đổi
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String newPhone = request.getPhone().trim();
            if (!newPhone.equalsIgnoreCase(existing.getPhone())) {
                clientRepository.findByPhone(newPhone).ifPresent(client -> {
                    if (!client.getId().equals(existing.getId())) {
                        throw new BusinessException(ClientError.CLIENT_PHONE_ALREADY_EXISTS);
                    }
                });
            }
        }

        // Kiểm tra email nếu thay đổi
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (existing.getEmail() == null || !newEmail.equalsIgnoreCase(existing.getEmail())) {
                clientRepository.findByEmail(newEmail).ifPresent(client -> {
                    if (!client.getId().equals(existing.getId())) {
                        throw new BusinessException(ClientError.CLIENT_EMAIL_ALREADY_EXISTS);
                    }
                });
            }
        }

        // Cập nhật các trường
        existing.setFullName(request.getFullName().trim());
        existing.setGender(request.getGender());
        existing.setDateOfBirth(request.getDateOfBirth());
        existing.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        existing.setEmail(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null);
        existing.setAddress(request.getAddress());
        existing.setAvatarUrl(request.getAvatarUrl());
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        existing.setHeight(request.getHeight());
        existing.setInitialWeight(request.getInitialWeight());
        existing.setFitnessGoal(request.getFitnessGoal());
        existing.setExperienceLevel(request.getExperienceLevel());
        existing.setTargetWeight(request.getTargetWeight());

        existing.setMedicalConditions(request.getMedicalConditions());
        existing.setInjuries(request.getInjuries());

        existing.setEmergencyContactName(request.getEmergencyContactName());
        existing.setEmergencyContactPhone(request.getEmergencyContactPhone());

        existing.setAssignedTrainerId(request.getAssignedTrainerId());
        existing.setNotes(request.getNotes());
        if (request.getJoinedDate() != null) {
            existing.setJoinedDate(request.getJoinedDate());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        ClientEntity saved = clientRepository.save(existing);
        return ClientResponseMapper.toResponse(saved);
    }
}
