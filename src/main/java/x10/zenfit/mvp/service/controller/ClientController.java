package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ICoreClientService;
import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;
import x10.zenfit.mvp.service.service.clientService.getPtClientsUc.GetPtClientsQuery;
import x10.zenfit.mvp.service.service.clientService.getPtClientsUc.PaginatedClientResponse;
import x10.zenfit.mvp.service.service.clientService.updateClientUc.UpdateClientRequest;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private static final Set<String> MANAGEMENT_ROLES = Set.of("ROLE_ADMIN", "ROLE_HEAD_OF_TRAINING", "ROLE_TEAM_MANAGER");

    private final ICoreClientService coreClientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ClientResponse> createClient(@Valid @RequestBody CreateClientRequest request) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        // PT thường chỉ được tạo khách hàng cho chính mình
        if (!isManagement || request.getAssignedTrainerId() == null || request.getAssignedTrainerId().isBlank()) {
            request.setAssignedTrainerId(currentUserId);
        }

        ClientResponse response = coreClientService.createClient(request);
        return ApiResponse.created(
                "CLIENT.CREATE_SUCCESS",
                "Thêm mới khách hàng thành công",
                response
        );
    }

    @GetMapping("/my-clients")
    public ApiResponse<PaginatedClientResponse> getMyClients(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ClientStatus status,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection
    ) {
        String trainerId = getCurrentUserId();

        GetPtClientsQuery query = GetPtClientsQuery.builder()
                .trainerId(trainerId)
                .page(page)
                .size(8)
                .search(search)
                .status(status)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PaginatedClientResponse response = coreClientService.getPtClients(query);

        return ApiResponse.ok(
                "CLIENT.GET_PAGINATED_SUCCESS",
                "Lấy danh sách khách hàng phân trang thành công",
                response
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ClientResponse> getClientById(@PathVariable String id) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        ClientResponse response = coreClientService.getClientById(id);

        // PT thường chỉ được xem chi tiết khách hàng của chính mình
        if (!isManagement && (response.getAssignedTrainerId() == null || !response.getAssignedTrainerId().equals(currentUserId))) {
            throw new BusinessException(ClientError.ACCESS_DENIED);
        }

        return ApiResponse.ok(
                "CLIENT.GET_DETAIL_SUCCESS",
                "Lấy thông tin chi tiết khách hàng thành công",
                response
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<ClientResponse>> searchClients(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trainerId
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();
        // Nếu là PT thường thì bắt buộc lọc theo currentUserId
        String effectiveTrainerId = isManagement ? trainerId : currentUserId;

        List<ClientResponse> response = coreClientService.searchClients(keyword, effectiveTrainerId);
        return ApiResponse.ok(
                "CLIENT.SEARCH_SUCCESS",
                "Tìm kiếm khách hàng thành công",
                response
        );
    }

    @GetMapping
    public ApiResponse<List<ClientResponse>> getAllClients(
            @RequestParam(required = false) String trainerId,
            @RequestParam(required = false) String search
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        // 💡 BẢO VỆ DỮ LIỆU: Nếu là PT thì BẮT BUỘC lấy khách hàng theo ID của chính PT đó (currentUserId),
        // tránh trường hợp account PT này xem được khách hàng của PT khác.
        // Chỉ ADMIN / Quản lý mới được truyền trainerId khác hoặc lấy tất cả (null).
        String effectiveTrainerId = isManagement ? trainerId : currentUserId;

        List<ClientResponse> response;
        if (search != null && !search.isBlank()) {
            response = coreClientService.searchClients(search, effectiveTrainerId);
        } else {
            response = coreClientService.getAllClients(effectiveTrainerId);
        }
        return ApiResponse.ok(
                "CLIENT.GET_ALL_SUCCESS",
                "Lấy danh sách khách hàng thành công",
                response
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<ClientResponse> updateClient(
            @PathVariable String id,
            @Valid @RequestBody UpdateClientRequest request
    ) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        if (!isManagement) {
            ClientResponse existing = coreClientService.getClientById(id);
            if (existing.getAssignedTrainerId() == null || !existing.getAssignedTrainerId().equals(currentUserId)) {
                throw new BusinessException(ClientError.ACCESS_DENIED);
            }
            request.setAssignedTrainerId(currentUserId);
        }

        ClientResponse response = coreClientService.updateClient(id, request);
        return ApiResponse.ok(
                "CLIENT.UPDATE_SUCCESS",
                "Cập nhật thông tin khách hàng thành công",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClient(@PathVariable String id) {
        String currentUserId = getCurrentUserId();
        boolean isManagement = isManagementUser();

        if (!isManagement) {
            ClientResponse existing = coreClientService.getClientById(id);
            if (existing.getAssignedTrainerId() == null || !existing.getAssignedTrainerId().equals(currentUserId)) {
                throw new BusinessException(ClientError.ACCESS_DENIED);
            }
        }

        coreClientService.deleteClient(id);
        return ApiResponse.ok(
                "CLIENT.DELETE_SUCCESS",
                "Xóa khách hàng thành công",
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
