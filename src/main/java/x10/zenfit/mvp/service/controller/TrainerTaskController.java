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
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;
import x10.zenfit.mvp.service.service.trainerTaskService.ICoreTrainerTaskService;
import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;
import x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc.CreateTrainerTaskRequest;
import x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc.TrainerTaskFilterQuery;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc.UpdateTrainerTaskStatusRequest;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc.UpdateTrainerTaskRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/trainer-tasks")
@RequiredArgsConstructor
public class TrainerTaskController {

    private final ICoreTrainerTaskService coreTrainerTaskService;

    private static final Set<String> MANAGEMENT_ROLES = Set.of(
            "ROLE_ADMIN", "ROLE_HEAD_OF_TRAINING", "ROLE_TEAM_MANAGER", "ROLE_STAFF"
    );

    // 1. Tạo công việc / hoạt động mới
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TrainerTaskResponse> createTask(
            @Valid @RequestBody CreateTrainerTaskRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainerTaskResponse response = coreTrainerTaskService.create(request, currentUserId, isManagement);

        return ApiResponse.created(
                "TRAINER_TASK.CREATE_SUCCESS",
                "Tạo công việc / hoạt động thành công",
                response
        );
    }

    // 2. Lấy danh sách công việc / hoạt động
    @GetMapping
    public ApiResponse<List<TrainerTaskResponse>> getTasks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) TrainerTaskStatus status,
            @RequestParam(required = false) String trainerId
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainerTaskFilterQuery query = TrainerTaskFilterQuery.builder()
                .taskDate(date)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .trainerId(trainerId)
                .build();

        List<TrainerTaskResponse> responses = coreTrainerTaskService.getTasks(query, currentUserId, isManagement);

        return ApiResponse.ok(
                "TRAINER_TASK.GET_LIST_SUCCESS",
                "Lấy danh sách công việc thành công",
                responses
        );
    }

    // 3. Lấy chi tiết công việc theo ID
    @GetMapping("/{id}")
    public ApiResponse<TrainerTaskResponse> getTaskById(@PathVariable String id) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainerTaskResponse response = coreTrainerTaskService.getById(id, currentUserId, isManagement);

        return ApiResponse.ok(
                "TRAINER_TASK.GET_DETAIL_SUCCESS",
                "Lấy chi tiết công việc thành công",
                response
        );
    }

    // 4. Cập nhật thông tin công việc
    @PatchMapping("/{id}")
    public ApiResponse<TrainerTaskResponse> updateTask(
            @PathVariable String id,
            @Valid @RequestBody UpdateTrainerTaskRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainerTaskResponse response = coreTrainerTaskService.update(id, request, currentUserId, isManagement);

        return ApiResponse.ok(
                "TRAINER_TASK.UPDATE_SUCCESS",
                "Cập nhật công việc thành công",
                response
        );
    }

    // 5. Cập nhật nhanh trạng thái (TODO, DONE, SKIPPED)
    @PatchMapping("/{id}/status")
    public ApiResponse<TrainerTaskResponse> updateTaskStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateTrainerTaskStatusRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        TrainerTaskResponse response = coreTrainerTaskService.updateStatus(id, request, currentUserId, isManagement);

        return ApiResponse.ok(
                "TRAINER_TASK.UPDATE_STATUS_SUCCESS",
                "Cập nhật trạng thái công việc thành công",
                response
        );
    }

    // 6. Xóa công việc
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTask(@PathVariable String id) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        coreTrainerTaskService.delete(id, currentUserId, isManagement);

        return ApiResponse.ok(
                "TRAINER_TASK.DELETE_SUCCESS",
                "Xóa công việc thành công",
                null
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

