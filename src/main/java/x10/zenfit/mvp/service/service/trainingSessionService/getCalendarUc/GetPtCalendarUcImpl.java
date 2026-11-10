package x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainerTaskRepository;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.session.TrainingSessionError;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPtCalendarUcImpl implements IGetPtCalendarUc {

    private final ITrainingSessionRepository trainingSessionRepository;
    private final ITrainerTaskRepository trainerTaskRepository;

    @Override
    public TrainingSessionCalendarResponse getPtCalendar(PtCalendarQuery query, String currentUserId, boolean isManagement) {
        if (query == null || query.getDate() == null || query.getView() == null) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        LocalDate date = query.getDate();
        LocalDate startDate;
        LocalDate endDate;

        switch (query.getView()) {
            case DAY -> {
                startDate = date;
                endDate = date;
            }
            case WEEK -> {
                startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            }
            case MONTH -> {
                startDate = date.with(TemporalAdjusters.firstDayOfMonth());
                endDate = date.with(TemporalAdjusters.lastDayOfMonth());
            }
            default -> throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        // Áp dụng cô lập dữ liệu
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

        List<TrainingSessionEntity> sessions = trainingSessionRepository.findSessionsForCalendar(
                targetTrainerId,
                targetClientId,
                targetContractId,
                query.getStatus(),
                startDate,
                endDate
        );

        List<TrainingSessionCalendarItemResponse> items = sessions.stream()
                .map(TrainingSessionCalendarItemResponse::fromEntity)
                .toList();

        // Lấy danh sách công việc / hoạt động của PT trong khoảng thời gian lịch
        List<TrainerTaskResponse> tasks = List.of();
        if (targetTrainerId != null && !targetTrainerId.isBlank()) {
            tasks = trainerTaskRepository.findTasks(targetTrainerId, null, startDate, endDate, null)
                    .stream()
                    .map(TrainerTaskResponse::fromEntity)
                    .toList();
        } else if (isManagement && (query.getClientId() == null || query.getClientId().isBlank())) {
            tasks = trainerTaskRepository.findTasks(null, null, startDate, endDate, null)
                    .stream()
                    .map(TrainerTaskResponse::fromEntity)
                    .toList();
        }

        return TrainingSessionCalendarResponse.builder()
                .view(query.getView())
                .startDate(startDate)
                .endDate(endDate)
                .totalSessions(items.size())
                .sessions(items)
                .tasks(tasks)
                .build();
    }
}

