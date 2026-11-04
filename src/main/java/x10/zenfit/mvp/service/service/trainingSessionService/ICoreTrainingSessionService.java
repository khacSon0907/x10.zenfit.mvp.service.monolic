package x10.zenfit.mvp.service.service.trainingSessionService;

import x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc.CompleteTrainingSessionRequest;
import x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc.CreateTrainingSessionRequest;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.PtCalendarQuery;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.TrainingSessionCalendarResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.PaginatedTrainingSessionResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.TrainingSessionFilterQuery;
import x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc.UpdateTrainingSessionRequest;

public interface ICoreTrainingSessionService {

    TrainingSessionResponse createTrainingSession(CreateTrainingSessionRequest request, String currentUserId, boolean isManagement);

    TrainingSessionResponse updateTrainingSession(String id, UpdateTrainingSessionRequest request, String currentUserId, boolean isManagement);

    TrainingSessionResponse getTrainingSessionById(String id, String currentUserId, boolean isManagement);

    PaginatedTrainingSessionResponse getTrainingSessions(TrainingSessionFilterQuery query, String currentUserId, boolean isManagement);

    TrainingSessionCalendarResponse getPtCalendar(PtCalendarQuery query, String currentUserId, boolean isManagement);

    TrainingSessionResponse completeTrainingSession(String id, CompleteTrainingSessionRequest request, String currentUserId, boolean isManagement);

    default TrainingSessionResponse completeTrainingSession(String id, String currentUserId, boolean isManagement) {
        return completeTrainingSession(id, null, currentUserId, isManagement);
    }

    TrainingSessionResponse noShowTrainingSession(String id, String currentUserId, boolean isManagement);

    TrainingSessionResponse cancelTrainingSession(String id, String currentUserId, boolean isManagement);
}
