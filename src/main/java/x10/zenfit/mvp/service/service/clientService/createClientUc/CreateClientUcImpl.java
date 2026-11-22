package x10.zenfit.mvp.service.service.clientService.createClientUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateClientUcImpl implements ICreateClientUc {

    private final IClientRepository clientRepository;
    private final IUserSubscriptionRepository userSubscriptionRepository;

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

        // Kiểm tra giới hạn học viên theo gói dịch vụ (UserSubscription) của PT
        String trainerId = request.getAssignedTrainerId();
        UserSubscriptionEntity activeSubscription = null;
        if (trainerId != null && !trainerId.isBlank()) {
            activeSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(trainerId.trim())
                    .orElseThrow(() -> new BusinessException(ClientError.SUBSCRIPTION_REQUIRED));

            // Kiểm tra gói đã hết hạn hay chưa
            if (activeSubscription.getEndAt() != null && activeSubscription.getEndAt().isBefore(Instant.now())) {
                log.warn("Subscription for trainer {} has expired at {}", trainerId, activeSubscription.getEndAt());
                throw new BusinessException(ClientError.SUBSCRIPTION_REQUIRED);
            }

            Integer maxStudents = activeSubscription.getMaxStudents();
            if (maxStudents != null && maxStudents > 0) {
                long currentCount = clientRepository.countByAssignedTrainerId(trainerId.trim());
                if (currentCount >= maxStudents) {
                    log.warn("Trainer {} exceeded maxStudents quota ({}/{})", trainerId, currentCount, maxStudents);
                    throw new BusinessException(ClientError.STUDENT_LIMIT_EXCEEDED);
                }
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
                .frontPhotoUrl(request.getFrontPhotoUrl())
                .backPhotoUrl(request.getBackPhotoUrl())
                .sidePhotoUrl(request.getSidePhotoUrl())
                .height(request.getHeight())
                .initialWeight(request.getInitialWeight())
                .assignedTrainerId(request.getAssignedTrainerId())
                .notes(request.getNotes())
                .joinedDate(request.getJoinedDate() != null ? request.getJoinedDate() : LocalDate.now())
                .createdAt(now)
                .updatedAt(now)
                .build();

        ClientEntity saved = clientRepository.save(entity);

        // Cập nhật số học viên hiện tại trong subscription của PT
        if (activeSubscription != null && trainerId != null) {
            long newCount = clientRepository.countByAssignedTrainerId(trainerId.trim());
            activeSubscription.setCurrentStudents((int) newCount);
            activeSubscription.setUpdatedAt(Instant.now());
            userSubscriptionRepository.save(activeSubscription);
        }

        return ClientResponseMapper.toResponse(saved);
    }
}
