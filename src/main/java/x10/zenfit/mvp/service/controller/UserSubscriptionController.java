package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;
import x10.zenfit.mvp.service.service.userSubscriptionService.ICoreUserSubscriptionService;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc.UserSubscriptionFilterQuery;
import x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc.SelectPlanRequest;
import x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc.UpdateUserSubscriptionStatusRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user-subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final ICoreUserSubscriptionService userSubscriptionService;

    private static final Set<String> MANAGEMENT_ROLES = Set.of(
            "ROLE_ADMIN", "ROLE_HEAD_OF_TRAINING", "ROLE_TEAM_MANAGER", "ROLE_STAFF"
    );

    // 1. User chọn và kích hoạt gói đăng ký (free hoặc mua gói)
    @PostMapping("/select")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserSubscriptionResponse> selectPlan(
            @Valid @RequestBody SelectPlanRequest request
    ) {
        String currentUserId = getCurrentUserId();
        UserSubscriptionResponse response = userSubscriptionService.selectPlan(currentUserId, request);
        return ApiResponse.created(
                "USER_SUBSCRIPTION.SELECT_SUCCESS",
                "Đăng ký / chọn gói dịch vụ thành công",
                response
        );
    }

    // 2. Lấy thông tin gói dịch vụ hiện tại đang hoạt động của tôi
    @GetMapping("/me")
    public ApiResponse<UserSubscriptionResponse> getMySubscription() {
        String currentUserId = getCurrentUserId();
        UserSubscriptionResponse response = userSubscriptionService.getMySubscription(currentUserId);
        return ApiResponse.ok(
                "USER_SUBSCRIPTION.GET_MY_SUCCESS",
                "Lấy thông tin gói dịch vụ hiện tại thành công",
                response
        );
    }

    // 3. Lấy lịch sử tất cả các gói dịch vụ tôi từng đăng ký
    @GetMapping("/my-history")
    public ApiResponse<List<UserSubscriptionResponse>> getMySubscriptionHistory() {
        String currentUserId = getCurrentUserId();
        List<UserSubscriptionResponse> response = userSubscriptionService.getMySubscriptionHistory(currentUserId);
        return ApiResponse.ok(
                "USER_SUBSCRIPTION.GET_HISTORY_SUCCESS",
                "Lấy lịch sử đăng ký gói dịch vụ thành công",
                response
        );
    }

    // 4. Người dùng hủy gói dịch vụ hiện tại của mình
    @PostMapping("/me/cancel")
    public ApiResponse<UserSubscriptionResponse> cancelMySubscription() {
        String currentUserId = getCurrentUserId();
        UserSubscriptionResponse response = userSubscriptionService.cancelMySubscription(currentUserId);
        return ApiResponse.ok(
                "USER_SUBSCRIPTION.CANCEL_SUCCESS",
                "Hủy gói dịch vụ thành công",
                response
        );
    }

    // 5. Admin lấy danh sách phân trang và lọc gói đăng ký của người dùng (Chỉ ADMIN)
    @GetMapping("/admin")
    public ApiResponse<PaginatedResult<UserSubscriptionResponse>> getAdminSubscriptions(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String planId,
            @RequestParam(required = false) UserSubscriptionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserSubscriptionFilterQuery query = UserSubscriptionFilterQuery.builder()
                .userId(userId)
                .planId(planId)
                .status(status)
                .page(page)
                .size(size)
                .build();

        PaginatedResult<UserSubscriptionResponse> response = userSubscriptionService.getAdminSubscriptions(query);
        return ApiResponse.ok(
                "USER_SUBSCRIPTION.ADMIN_LIST_SUCCESS",
                "Lấy danh sách đăng ký gói dịch vụ thành công",
                response
        );
    }

    // 6. Xem chi tiết 1 subscription theo ID
    @GetMapping("/{id}")
    public ApiResponse<UserSubscriptionResponse> getSubscriptionById(@PathVariable String id) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();
        UserSubscriptionResponse response = userSubscriptionService.getSubscriptionById(id, currentUserId, isManagement);
        return ApiResponse.ok(
                "USER_SUBSCRIPTION.GET_DETAIL_SUCCESS",
                "Lấy chi tiết đăng ký gói thành công",
                response
        );
    }

    // 7. Admin cập nhật trạng thái subscription (Chỉ ADMIN)
    @PatchMapping("/{id}/status")
    public ApiResponse<UserSubscriptionResponse> updateSubscriptionStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserSubscriptionStatusRequest request
    ) {
        UserSubscriptionResponse response = userSubscriptionService.updateSubscriptionStatus(id, request);
        return ApiResponse.ok(
                "USER_SUBSCRIPTION.UPDATE_STATUS_SUCCESS",
                "Cập nhật trạng thái đăng ký thành công",
                response
        );
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng chưa đăng nhập");
        }

        return authentication.getName();
    }

    private boolean isManagementUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(MANAGEMENT_ROLES::contains);
    }
}

