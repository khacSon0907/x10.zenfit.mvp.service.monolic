package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;
import x10.zenfit.mvp.service.service.trainingSessionService.ICoreTrainingSessionService;
import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc.CompleteTrainingSessionRequest;
import x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc.CreateTrainingSessionRequest;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.CalendarViewType;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.PtCalendarQuery;
import x10.zenfit.mvp.service.service.trainingSessionService.getCalendarUc.TrainingSessionCalendarResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.PaginatedTrainingSessionResponse;
import x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc.TrainingSessionFilterQuery;
import x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc.UpdateTrainingSessionRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api/training-sessions")
@RequiredArgsConstructor
public class TrainingSessionController {

    private final ICoreTrainingSessionService coreTrainingSessionService;

    private static final Set<String> MANAGEMENT_ROLES = Set.of(
            "ROLE_ADMIN", "ROLE_HEAD_OF_TRAINING", "ROLE_TEAM_MANAGER", "ROLE_STAFF"
    );

    // 1. Tạo buổi tập mới
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TrainingSessionResponse> createTrainingSession(
            @Valid @RequestBody CreateTrainingSessionRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionResponse response = coreTrainingSessionService.createTrainingSession(request, currentUserId, isManagement);

        return ApiResponse.created(
                "SESSION.CREATE_SUCCESS",
                "Tạo buổi tập thành công",
                response
        );
    }

    // 2. Lấy lịch làm việc PT theo Ngày, Tuần, Tháng (Calendar)
    @GetMapping("/calendar")
    public ApiResponse<TrainingSessionCalendarResponse> getPtCalendar(
            @RequestParam CalendarViewType view,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String trainerId,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String contractId,
            @RequestParam(required = false) TrainingSessionStatus status
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        PtCalendarQuery query = PtCalendarQuery.builder()
                .view(view)
                .date(date)
                .trainerId(trainerId)
                .clientId(clientId)
                .contractId(contractId)
                .status(status)
                .build();

        TrainingSessionCalendarResponse response = coreTrainingSessionService.getPtCalendar(query, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.CALENDAR_SUCCESS",
                "Lấy lịch làm việc PT thành công",
                response
        );
    }

    // 3. Lấy chi tiết buổi tập theo ID
    @GetMapping("/{id}")
    public ApiResponse<TrainingSessionResponse> getTrainingSessionById(
            @PathVariable String id
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionResponse response = coreTrainingSessionService.getTrainingSessionById(id, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.GET_DETAIL_SUCCESS",
                "Lấy thông tin chi tiết buổi tập thành công",
                response
        );
    }

    // 4. Lấy danh sách buổi tập có phân trang và lọc
    @GetMapping
    public ApiResponse<PaginatedTrainingSessionResponse> getTrainingSessions(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String trainerId,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String contractId,
            @RequestParam(required = false) TrainingSessionStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionFilterQuery query = TrainingSessionFilterQuery.builder()
                .page(page)
                .size(size)
                .trainerId(trainerId)
                .clientId(clientId)
                .contractId(contractId)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        PaginatedTrainingSessionResponse response = coreTrainingSessionService.getTrainingSessions(query, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.GET_PAGINATED_SUCCESS",
                "Lấy danh sách buổi tập thành công",
                response
        );
    }

    // 5. Cập nhật thông tin buổi tập
    @PatchMapping("/{id}")
    public ApiResponse<TrainingSessionResponse> updateTrainingSession(
            @PathVariable String id,
            @Valid @RequestBody UpdateTrainingSessionRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionResponse response = coreTrainingSessionService.updateTrainingSession(id, request, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.UPDATE_SUCCESS",
                "Cập nhật thông tin buổi tập thành công",
                response
        );
    }

    // 6. Hoàn thành buổi tập
    @PatchMapping("/{id}/complete")
    public ApiResponse<TrainingSessionResponse> completeTrainingSession(
            @PathVariable String id,
            @RequestBody(required = false) CompleteTrainingSessionRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionResponse response = coreTrainingSessionService.completeTrainingSession(id, request, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.COMPLETE_SUCCESS",
                "Xác nhận hoàn thành buổi tập thành công",
                response
        );
    }

    // 7. Đánh dấu học viên vắng mặt
    @PatchMapping("/{id}/no-show")
    public ApiResponse<TrainingSessionResponse> noShowTrainingSession(
            @PathVariable String id
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionResponse response = coreTrainingSessionService.noShowTrainingSession(id, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.NO_SHOW_SUCCESS",
                "Đánh dấu học viên vắng mặt thành công",
                response
        );
    }

    // 9. Hủy buổi tập
    @PatchMapping("/{id}/cancel")
    public ApiResponse<TrainingSessionResponse> cancelTrainingSession(
            @PathVariable String id
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainingSessionResponse response = coreTrainingSessionService.cancelTrainingSession(id, currentUserId, isManagement);

        return ApiResponse.ok(
                "SESSION.CANCEL_SUCCESS",
                "Hủy buổi tập thành công",
                response
        );
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal() == null ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        return authentication.getPrincipal().toString();
    }

    private boolean isManagementUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (MANAGEMENT_ROLES.contains(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}

