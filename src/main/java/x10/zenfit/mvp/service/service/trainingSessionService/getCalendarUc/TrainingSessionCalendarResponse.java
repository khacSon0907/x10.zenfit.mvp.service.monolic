package x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionCalendarResponse {

    private CalendarViewType view;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalSessions;
    private List<TrainingSessionCalendarItemResponse> sessions;
}

