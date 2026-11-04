
package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.service.clientGoalService.ICoreClientGoalService;
import x10.zenfit.mvp.service.service.clientGoalService.createGoalUc.CreateClientGoalRequest;
import x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc.UpdateClientGoalRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/client-goals")
@RequiredArgsConstructor
public class ClientGoalController {

    private final ICoreClientGoalService coreClientGoalService;

    // Tạo mục tiêu mới cho khách hàng
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ClientGoalEntity> createClientGoal(
            @Valid @RequestBody CreateClientGoalRequest request
    ) {
        ClientGoalEntity response = coreClientGoalService.createClientGoal(request);
        return ApiResponse.created(
                "CLIENT_GOAL.CREATE_SUCCESS",
                "Tạo mục tiêu khách hàng thành công",
                response
        );
    }

    // Lấy chi tiết mục tiêu theo ID
    @GetMapping("/{id}")
    public ApiResponse<ClientGoalEntity> getClientGoalById(
            @PathVariable String id
    ) {
        ClientGoalEntity response = coreClientGoalService.getClientGoalById(id);
        return ApiResponse.ok(
                "CLIENT_GOAL.GET_DETAIL_SUCCESS",
                "Lấy thông tin chi tiết mục tiêu thành công",
                response
        );
    }

    // Lấy danh sách mục tiêu (hỗ trợ lọc theo clientId và status)
    @GetMapping
    public ApiResponse<List<ClientGoalEntity>> getAllClientGoals(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) GoalPhaseStatus status
    ) {
        List<ClientGoalEntity> response = coreClientGoalService.getAllClientGoals(clientId, status);
        return ApiResponse.ok(
                "CLIENT_GOAL.GET_ALL_SUCCESS",
                "Lấy danh sách mục tiêu khách hàng thành công",
                response
        );
    }

    // Lấy danh sách mục tiêu theo ID của client (clientEntity)
    @GetMapping("/client/{clientId}")
    public ApiResponse<List<ClientGoalEntity>> getClientGoalsByClientId(
            @PathVariable String clientId
    ) {
        List<ClientGoalEntity> response = coreClientGoalService.getClientGoalsByClientId(clientId);
        return ApiResponse.ok(
                "CLIENT_GOAL.GET_BY_CLIENT_SUCCESS",
                "Lấy danh sách mục tiêu theo khách hàng thành công",
                response
        );
    }

    // Lấy mục tiêu đang hoạt động (ACTIVE) theo ID của client (clientEntity)
    @GetMapping("/client/{clientId}/active")
    public ApiResponse<ClientGoalEntity> getActiveGoalByClientId(
            @PathVariable String clientId
    ) {
        ClientGoalEntity response = coreClientGoalService.getActiveGoalByClientId(clientId);
        return ApiResponse.ok(
                "CLIENT_GOAL.GET_ACTIVE_GOAL_SUCCESS",
                "Lấy mục tiêu đang hoạt động của khách hàng thành công",
                response
        );
    }

    // Cập nhật mục tiêu theo ID
    @PutMapping("/{id}")
    public ApiResponse<ClientGoalEntity> updateClientGoal(
            @PathVariable String id,
            @Valid @RequestBody UpdateClientGoalRequest request
    ) {
        ClientGoalEntity response = coreClientGoalService.updateClientGoal(id, request);
        return ApiResponse.ok(
                "CLIENT_GOAL.UPDATE_SUCCESS",
                "Cập nhật mục tiêu khách hàng thành công",
                response
        );
    }

    // Xóa mục tiêu theo ID
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClientGoal(
            @PathVariable String id
    ) {
        coreClientGoalService.deleteClientGoal(id);
        return ApiResponse.ok(
                "CLIENT_GOAL.DELETE_SUCCESS",
                "Xóa mục tiêu khách hàng thành công",
                null
        );
    }
}
