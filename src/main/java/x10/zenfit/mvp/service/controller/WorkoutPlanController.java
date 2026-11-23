package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.service.workoutPlanService.ICoreWorkoutPlanService;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.*;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final ICoreWorkoutPlanService coreWorkoutPlanService;

    // 1. Tạo giáo án tập Gym
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkoutPlanResponse> createPlan(
            @Valid @RequestBody CreateWorkoutPlanRequest request
    ) {
        String trainerId = getCurrentUserId();
        WorkoutPlanResponse response = coreWorkoutPlanService.createPlan(trainerId, request);
        return ApiResponse.created(
                "WORKOUT_PLAN.CREATE_SUCCESS",
                "Tạo giáo án tập luyện thành công",
                response
        );
    }

    // 2. Lấy danh sách giáo án của PT đang đăng nhập
    @GetMapping
    public ApiResponse<List<WorkoutPlanResponse>> getMyPlans() {
        String trainerId = getCurrentUserId();
        List<WorkoutPlanResponse> response = coreWorkoutPlanService.getTrainerPlans(trainerId);
        return ApiResponse.ok(
                "WORKOUT_PLAN.GET_ALL_SUCCESS",
                "Lấy danh sách giáo án tập luyện thành công",
                response
        );
    }

    // 3. Lấy chi tiết giáo án
    @GetMapping("/{planId}")
    public ApiResponse<WorkoutPlanResponse> getPlanDetail(
            @PathVariable String planId
    ) {
        String trainerId = getCurrentUserId();
        WorkoutPlanResponse response = coreWorkoutPlanService.getPlanDetail(trainerId, planId);
        return ApiResponse.ok(
                "WORKOUT_PLAN.GET_DETAIL_SUCCESS",
                "Lấy chi tiết giáo án tập luyện thành công",
                response
        );
    }

    // 4. Cập nhật thông tin chung giáo án
    @PutMapping("/{planId}")
    public ApiResponse<WorkoutPlanResponse> updatePlan(
            @PathVariable String planId,
            @Valid @RequestBody UpdateWorkoutPlanRequest request
    ) {
        String trainerId = getCurrentUserId();
        WorkoutPlanResponse response = coreWorkoutPlanService.updatePlan(trainerId, planId, request);
        return ApiResponse.ok(
                "WORKOUT_PLAN.UPDATE_SUCCESS",
                "Cập nhật giáo án tập luyện thành công",
                response
        );
    }

    // 5. Xóa giáo án
    @DeleteMapping("/{planId}")
    public ApiResponse<Void> deletePlan(
            @PathVariable String planId
    ) {
        String trainerId = getCurrentUserId();
        coreWorkoutPlanService.deletePlan(trainerId, planId);
        return ApiResponse.ok(
                "WORKOUT_PLAN.DELETE_SUCCESS",
                "Xóa giáo án tập luyện thành công",
                null
        );
    }

    // 6. Lấy danh sách giáo án của Client
    @GetMapping("/clients/{clientId}")
    public ApiResponse<List<WorkoutPlanResponse>> getClientPlans(
            @PathVariable String clientId
    ) {
        String trainerId = getCurrentUserId();
        List<WorkoutPlanResponse> response = coreWorkoutPlanService.getClientPlans(trainerId, clientId);
        return ApiResponse.ok(
                "WORKOUT_PLAN.GET_BY_CLIENT_SUCCESS",
                "Lấy danh sách giáo án của khách hàng thành công",
                response
        );
    }

    // 7. Thêm ngày tập vào giáo án
    @PostMapping("/{planId}/days")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkoutDayResponse> addDay(
            @PathVariable String planId,
            @Valid @RequestBody WorkoutDayRequest request
    ) {
        String trainerId = getCurrentUserId();
        WorkoutDayResponse response = coreWorkoutPlanService.addDay(trainerId, planId, request);
        return ApiResponse.created(
                "WORKOUT_PLAN.ADD_DAY_SUCCESS",
                "Thêm ngày tập vào giáo án thành công",
                response
        );
    }

    // 8. Cập nhật ngày tập
    @PutMapping("/{planId}/days/{dayId}")
    public ApiResponse<WorkoutDayResponse> updateDay(
            @PathVariable String planId,
            @PathVariable String dayId,
            @Valid @RequestBody UpdateWorkoutDayRequest request
    ) {
        String trainerId = getCurrentUserId();
        WorkoutDayResponse response = coreWorkoutPlanService.updateDay(trainerId, planId, dayId, request);
        return ApiResponse.ok(
                "WORKOUT_PLAN.UPDATE_DAY_SUCCESS",
                "Cập nhật ngày tập thành công",
                response
        );
    }

    // 9. Xóa ngày tập
    @DeleteMapping("/{planId}/days/{dayId}")
    public ApiResponse<Void> deleteDay(
            @PathVariable String planId,
            @PathVariable String dayId
    ) {
        String trainerId = getCurrentUserId();
        coreWorkoutPlanService.deleteDay(trainerId, planId, dayId);
        return ApiResponse.ok(
                "WORKOUT_PLAN.DELETE_DAY_SUCCESS",
                "Xóa ngày tập thành công",
                null
        );
    }

    // 10. Thêm bài tập vào ngày tập
    @PostMapping("/{planId}/days/{dayId}/exercises")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkoutExerciseResponse> addExercise(
            @PathVariable String planId,
            @PathVariable String dayId,
            @Valid @RequestBody WorkoutExerciseRequest request
    ) {
        String trainerId = getCurrentUserId();
        WorkoutExerciseResponse response = coreWorkoutPlanService.addExercise(trainerId, planId, dayId, request);
        return ApiResponse.created(
                "WORKOUT_PLAN.ADD_EXERCISE_SUCCESS",
                "Thêm bài tập vào ngày tập thành công",
                response
        );
    }

    // 11. Cập nhật bài tập
    @PutMapping("/{planId}/days/{dayId}/exercises/{exerciseId}")
    public ApiResponse<WorkoutExerciseResponse> updateExercise(
            @PathVariable String planId,
            @PathVariable String dayId,
            @PathVariable String exerciseId,
            @Valid @RequestBody UpdateWorkoutExerciseRequest request
    ) {
        String trainerId = getCurrentUserId();
        WorkoutExerciseResponse response = coreWorkoutPlanService.updateExercise(trainerId, planId, dayId, exerciseId, request);
        return ApiResponse.ok(
                "WORKOUT_PLAN.UPDATE_EXERCISE_SUCCESS",
                "Cập nhật bài tập thành công",
                response
        );
    }

    // 12. Xóa bài tập
    @DeleteMapping("/{planId}/days/{dayId}/exercises/{exerciseId}")
    public ApiResponse<Void> deleteExercise(
            @PathVariable String planId,
            @PathVariable String dayId,
            @PathVariable String exerciseId
    ) {
        String trainerId = getCurrentUserId();
        coreWorkoutPlanService.deleteExercise(trainerId, planId, dayId, exerciseId);
        return ApiResponse.ok(
                "WORKOUT_PLAN.DELETE_EXERCISE_SUCCESS",
                "Xóa bài tập thành công",
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
}

