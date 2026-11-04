package x10.zenfit.mvp.service.service.trainingSessionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.trainingSessionService.cancelSessionUc.ICancelTrainingSessionUc;
import x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc.CompleteTrainingSessionRequest;
import x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc.ICompleteTrainingSessionUc;
import x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc.CreateTrainingSessionRequest;
import x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc.ICreateTrainingSessionUc;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.IGetPtCalendarUc;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.PtCalendarQuery;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.TrainingSessionCalendarResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionUc.IGetTrainingSessionUc;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.IGetTrainingSessionsUc;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.PaginatedTrainingSessionResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.TrainingSessionFilterQuery;
import x10.zenfit.mvp.service.service.trainingSessionService.noShowSessionUc.INoShowTrainingSessionUc;
import x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc.IUpdateTrainingSessionUc;
import x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc.UpdateTrainingSessionRequest;

@Service
@RequiredArgsConstructor
public class CoreTrainingSessionServiceImpl implements ICoreTrainingSessionService {

    private final ICreateTrainingSessionUc createTrainingSessionUc;
    private final IUpdateTrainingSessionUc updateTrainingSessionUc;
    private final IGetTrainingSessionUc getTrainingSessionUc;
    private final IGetTrainingSessionsUc getTrainingSessionsUc;
    private final IGetPtCalendarUc getPtCalendarUc;
    private final ICompleteTrainingSessionUc completeTrainingSessionUc;
    private final INoShowTrainingSessionUc noShowTrainingSessionUc;
    private final ICancelTrainingSessionUc cancelTrainingSessionUc;

    @Override
    public TrainingSessionResponse createTrainingSession(CreateTrainingSessionRequest request, String currentUserId, boolean isManagement) {
        return createTrainingSessionUc.createTrainingSession(request, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionResponse updateTrainingSession(String id, UpdateTrainingSessionRequest request, String currentUserId, boolean isManagement) {
        return updateTrainingSessionUc.updateTrainingSession(id, request, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionResponse getTrainingSessionById(String id, String currentUserId, boolean isManagement) {
        return getTrainingSessionUc.getTrainingSessionById(id, currentUserId, isManagement);
    }

    @Override
    public PaginatedTrainingSessionResponse getTrainingSessions(TrainingSessionFilterQuery query, String currentUserId, boolean isManagement) {
        return getTrainingSessionsUc.getTrainingSessions(query, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionCalendarResponse getPtCalendar(PtCalendarQuery query, String currentUserId, boolean isManagement) {
        return getPtCalendarUc.getPtCalendar(query, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionResponse completeTrainingSession(String id, CompleteTrainingSessionRequest request, String currentUserId, boolean isManagement) {
        return completeTrainingSessionUc.complete(id, request, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionResponse completeTrainingSession(String id, String currentUserId, boolean isManagement) {
        return completeTrainingSessionUc.complete(id, null, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionResponse noShowTrainingSession(String id, String currentUserId, boolean isManagement) {
        return noShowTrainingSessionUc.noShow(id, currentUserId, isManagement);
    }

    @Override
    public TrainingSessionResponse cancelTrainingSession(String id, String currentUserId, boolean isManagement) {
        return cancelTrainingSessionUc.cancel(id, currentUserId, isManagement);
    }
}
