package x10.zenfit.mvp.service.service.clientService.deleteClientUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeleteClientUcImpl implements IDeleteClientUc {

    private final IClientRepository clientRepository;
    private final IUserSubscriptionRepository userSubscriptionRepository;

    @Override
    public void deleteClient(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Client ID là bắt buộc");
        }

        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        String trainerId = client.getAssignedTrainerId();

        clientRepository.deleteById(id);

        // Cập nhật lại số học viên trong subscription của PT sau khi xóa
        if (trainerId != null && !trainerId.isBlank()) {
            userSubscriptionRepository.findActiveSubscriptionByUserId(trainerId).ifPresent(sub -> {
                long remainingCount = clientRepository.countByAssignedTrainerId(trainerId);
                sub.setCurrentStudents((int) remainingCount);
                sub.setUpdatedAt(Instant.now());
                userSubscriptionRepository.save(sub);
            });
        }
    }
}
