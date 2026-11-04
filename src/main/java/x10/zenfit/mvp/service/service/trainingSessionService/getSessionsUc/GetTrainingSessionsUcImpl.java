package x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTrainingSessionsUcImpl implements IGetTrainingSessionsUc {

    private final ITrainingSessionRepository trainingSessionRepository;

    @Override
    public PaginatedTrainingSessionResponse getTrainingSessions(TrainingSessionFilterQuery query, String currentUserId, boolean isManagement) {
        if (query == null) {
            query = TrainingSessionFilterQuery.builder().build();
        }

        int page = Math.max(1, query.getPage());
        int size = Math.max(1, query.getSize());

        // Áp dụng cô lập dữ liệu PT
        String targetTrainerId;
        if (!isManagement) {
            targetTrainerId = currentUserId;
        } else {
            targetTrainerId = (query.getTrainerId() != null && !query.getTrainerId().isBlank())
                    ? query.getTrainerId().trim()
                    : null;
        }

        String targetClientId = (query.getClientId() != null && !query.getClientId().isBlank())
                ? query.getClientId().trim()
                : null;

        String targetContractId = (query.getContractId() != null && !query.getContractId().isBlank())
                ? query.getContractId().trim()
                : null;

        long totalElements = trainingSessionRepository.countSessionsWithFilter(
                targetTrainerId,
                targetClientId,
                targetContractId,
                query.getStatus(),
                query.getStartDate(),
                query.getEndDate()
        );

        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;

        if (totalElements == 0 || (long) (page - 1) * size >= totalElements) {
            return PaginatedTrainingSessionResponse.builder()
                    .items(Collections.emptyList())
                    .page(page)
                    .size(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .first(page == 1)
                    .last(true)
                    .build();
        }

        List<TrainingSessionEntity> entities = trainingSessionRepository.findSessionsWithFilter(
                targetTrainerId,
                targetClientId,
                targetContractId,
                query.getStatus(),
                query.getStartDate(),
                query.getEndDate(),
                page,
                size
        );

        List<TrainingSessionResponse> items = entities.stream()
                .map(TrainingSessionResponse::fromEntity)
                .toList();

        return PaginatedTrainingSessionResponse.builder()
                .items(items)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(page == 1)
                .last(page >= totalPages)
                .build();
    }
}

