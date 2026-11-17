package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;
import x10.zenfit.mvp.service.service.subscriptionPlanService.ICoreSubscriptionPlanService;
import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;
import x10.zenfit.mvp.service.service.subscriptionPlanService.createPlanUc.CreateSubscriptionPlanRequest;
import x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc.SubscriptionPlanFilterQuery;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc.UpdateSubscriptionPlanStatusRequest;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanUc.UpdateSubscriptionPlanRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final ICoreSubscriptionPlanService subscriptionPlanService;

    // 1. Tạo mới gói dịch vụ (Chỉ ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SubscriptionPlanResponse> createPlan(
            @Valid @RequestBody CreateSubscriptionPlanRequest request
    ) {
        SubscriptionPlanResponse response = subscriptionPlanService.createPlan(request);
        return ApiResponse.created(
                "SUBSCRIPTION_PLAN.CREATE_SUCCESS",
                "Tạo gói dịch vụ thành công",
                response
        );
    }

    // 2. Lấy danh sách các gói dịch vụ đang hoạt động (ACTIVE) cho PT / User xem
    @GetMapping
    public ApiResponse<List<SubscriptionPlanResponse>> getActivePlans() {
        List<SubscriptionPlanResponse> response = subscriptionPlanService.getActivePlans();
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.GET_ACTIVE_SUCCESS",
                "Lấy danh sách gói dịch vụ thành công",
                response
        );
    }

    // 3. Lấy thông tin gói mặc định miễn phí hiện tại
    @GetMapping("/default")
    public ApiResponse<SubscriptionPlanResponse> getDefaultPlan() {
        SubscriptionPlanResponse response = subscriptionPlanService.getDefaultPlan();
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.GET_DEFAULT_SUCCESS",
                "Lấy gói dịch vụ mặc định thành công",
                response
        );
    }

    // 4. Lấy danh sách phân trang và lọc cho Quản trị viên (Chỉ ADMIN)
    @GetMapping("/admin")
    public ApiResponse<PaginatedResult<SubscriptionPlanResponse>> getAdminPlans(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SubscriptionPlanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SubscriptionPlanFilterQuery query = SubscriptionPlanFilterQuery.builder()
                .keyword(keyword)
                .status(status)
                .page(page)
                .size(size)
                .build();

        PaginatedResult<SubscriptionPlanResponse> response = subscriptionPlanService.getAdminPlans(query);
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.GET_ADMIN_LIST_SUCCESS",
                "Lấy danh sách quản trị gói dịch vụ thành công",
                response
        );
    }

    // 5. Xem chi tiết gói dịch vụ
    @GetMapping("/{id}")
    public ApiResponse<SubscriptionPlanResponse> getPlanById(@PathVariable String id) {
        SubscriptionPlanResponse response = subscriptionPlanService.getPlanById(id);
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.GET_DETAIL_SUCCESS",
                "Lấy chi tiết gói dịch vụ thành công",
                response
        );
    }

    // 6. Cập nhật thông tin gói dịch vụ (Chỉ ADMIN)
    @PutMapping("/{id}")
    public ApiResponse<SubscriptionPlanResponse> updatePlan(
            @PathVariable String id,
            @Valid @RequestBody UpdateSubscriptionPlanRequest request
    ) {
        SubscriptionPlanResponse response = subscriptionPlanService.updatePlan(id, request);
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.UPDATE_SUCCESS",
                "Cập nhật gói dịch vụ thành công",
                response
        );
    }

    // 7. Cập nhật trạng thái gói dịch vụ (Chỉ ADMIN)
    @PatchMapping("/{id}/status")
    public ApiResponse<SubscriptionPlanResponse> updatePlanStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateSubscriptionPlanStatusRequest request
    ) {
        SubscriptionPlanResponse response = subscriptionPlanService.updatePlanStatus(id, request);
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.UPDATE_STATUS_SUCCESS",
                "Cập nhật trạng thái gói dịch vụ thành công",
                response
        );
    }

    // 8. Xóa gói dịch vụ (Chỉ ADMIN)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePlan(@PathVariable String id) {
        subscriptionPlanService.deletePlan(id);
        return ApiResponse.ok(
                "SUBSCRIPTION_PLAN.DELETE_SUCCESS",
                "Xóa gói dịch vụ thành công",
                null
        );
    }
}

