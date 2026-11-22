package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.service.nutritionPlanService.ICoreNutritionPlanService;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.*;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nutrition-plans")
@RequiredArgsConstructor
public class NutritionPlanController {

    private final ICoreNutritionPlanService coreNutritionPlanService;

    // 1. Tạo kế hoạch dinh dưỡng
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NutritionPlanResponse> createPlan(
            @Valid @RequestBody CreateNutritionPlanRequest request
    ) {
        String trainerId = getCurrentUserId();
        NutritionPlanResponse response = coreNutritionPlanService.createPlan(trainerId, request);
        return ApiResponse.created(
                "NUTRITION_PLAN.CREATE_SUCCESS",
                "Tạo kế hoạch dinh dưỡng thành công",
                response
        );
    }

    // 2. Lấy danh sách kế hoạch của PT đang đăng nhập
    @GetMapping
    public ApiResponse<List<NutritionPlanResponse>> getMyPlans() {
        String trainerId = getCurrentUserId();
        List<NutritionPlanResponse> response = coreNutritionPlanService.getTrainerPlans(trainerId);
        return ApiResponse.ok(
                "NUTRITION_PLAN.GET_ALL_SUCCESS",
                "Lấy danh sách kế hoạch dinh dưỡng thành công",
                response
        );
    }

    // 3. Lấy chi tiết kế hoạch
    @GetMapping("/{planId}")
    public ApiResponse<NutritionPlanResponse> getPlanDetail(
            @PathVariable String planId
    ) {
        String trainerId = getCurrentUserId();
        NutritionPlanResponse response = coreNutritionPlanService.getPlanDetail(trainerId, planId);
        return ApiResponse.ok(
                "NUTRITION_PLAN.GET_DETAIL_SUCCESS",
                "Lấy chi tiết kế hoạch dinh dưỡng thành công",
                response
        );
    }

    // 4. Cập nhật kế hoạch
    @PutMapping("/{planId}")
    public ApiResponse<NutritionPlanResponse> updatePlan(
            @PathVariable String planId,
            @Valid @RequestBody UpdateNutritionPlanRequest request
    ) {
        String trainerId = getCurrentUserId();
        NutritionPlanResponse response = coreNutritionPlanService.updatePlan(trainerId, planId, request);
        return ApiResponse.ok(
                "NUTRITION_PLAN.UPDATE_SUCCESS",
                "Cập nhật kế hoạch dinh dưỡng thành công",
                response
        );
    }

    // 5. Xóa kế hoạch
    @DeleteMapping("/{planId}")
    public ApiResponse<Void> deletePlan(
            @PathVariable String planId
    ) {
        String trainerId = getCurrentUserId();
        coreNutritionPlanService.deletePlan(trainerId, planId);
        return ApiResponse.ok(
                "NUTRITION_PLAN.DELETE_SUCCESS",
                "Xóa kế hoạch dinh dưỡng thành công",
                null
        );
    }

    // 6. Lấy danh sách kế hoạch của Client
    @GetMapping("/clients/{clientId}")
    public ApiResponse<List<NutritionPlanResponse>> getClientPlans(
            @PathVariable String clientId
    ) {
        String trainerId = getCurrentUserId();
        List<NutritionPlanResponse> response = coreNutritionPlanService.getClientPlans(trainerId, clientId);
        return ApiResponse.ok(
                "NUTRITION_PLAN.GET_BY_CLIENT_SUCCESS",
                "Lấy danh sách kế hoạch dinh dưỡng của khách hàng thành công",
                response
        );
    }

    // 7. Thêm món ăn vào kế hoạch
    @PostMapping("/{planId}/foods")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NutritionFoodResponse> addFood(
            @PathVariable String planId,
            @Valid @RequestBody NutritionFoodRequest request
    ) {
        String trainerId = getCurrentUserId();
        NutritionFoodResponse response = coreNutritionPlanService.addFood(trainerId, planId, request);
        return ApiResponse.created(
                "NUTRITION_PLAN.ADD_FOOD_SUCCESS",
                "Thêm món ăn vào kế hoạch thành công",
                response
        );
    }

    // 8. Cập nhật món ăn
    @PutMapping("/{planId}/foods/{foodId}")
    public ApiResponse<NutritionFoodResponse> updateFood(
            @PathVariable String planId,
            @PathVariable String foodId,
            @Valid @RequestBody UpdateNutritionFoodRequest request
    ) {
        String trainerId = getCurrentUserId();
        NutritionFoodResponse response = coreNutritionPlanService.updateFood(trainerId, planId, foodId, request);
        return ApiResponse.ok(
                "NUTRITION_PLAN.UPDATE_FOOD_SUCCESS",
                "Cập nhật món ăn thành công",
                response
        );
    }

    // 9. Xóa món ăn
    @DeleteMapping("/{planId}/foods/{foodId}")
    public ApiResponse<Void> deleteFood(
            @PathVariable String planId,
            @PathVariable String foodId
    ) {
        String trainerId = getCurrentUserId();
        coreNutritionPlanService.deleteFood(trainerId, planId, foodId);
        return ApiResponse.ok(
                "NUTRITION_PLAN.DELETE_FOOD_SUCCESS",
                "Xóa món ăn thành công",
                null
        );
    }

    // 10. Cập nhật Macro mục tiêu trong ngày
    @PutMapping("/{planId}/macro-target")
    public ApiResponse<DailyMacroTargetResponse> updateMacroTarget(
            @PathVariable String planId,
            @Valid @RequestBody UpdateMacroTargetRequest request
    ) {
        String trainerId = getCurrentUserId();
        DailyMacroTargetResponse response = coreNutritionPlanService.updateMacroTarget(trainerId, planId, request);
        return ApiResponse.ok(
                "NUTRITION_PLAN.UPDATE_MACRO_TARGET_SUCCESS",
                "Cập nhật Macro mục tiêu thành công",
                response
        );
    }

    // 11. Lấy thống kê tổng Calories, Protein, Carbs, Fat và % hoàn thành mục tiêu
    @GetMapping("/{planId}/macro-summary")
    public ApiResponse<NutritionMacroSummaryResponse> getMacroSummary(
            @PathVariable String planId
    ) {
        String trainerId = getCurrentUserId();
        NutritionMacroSummaryResponse response = coreNutritionPlanService.getMacroSummary(trainerId, planId);
        return ApiResponse.ok(
                "NUTRITION_PLAN.GET_MACRO_SUMMARY_SUCCESS",
                "Lấy thống kê dinh dưỡng thành công",
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
}

