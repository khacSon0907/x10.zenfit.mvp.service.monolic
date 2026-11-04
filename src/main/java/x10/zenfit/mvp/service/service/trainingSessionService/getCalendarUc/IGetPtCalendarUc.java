package x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc;

public interface IGetPtCalendarUc {
    TrainingSessionCalendarResponse getPtCalendar(PtCalendarQuery query, String currentUserId, boolean isManagement);
}

