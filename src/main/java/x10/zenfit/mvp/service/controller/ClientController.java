package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ICoreClientService;
import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;
import x10.zenfit.mvp.service.service.clientService.updateClientUc.UpdateClientRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ICoreClientService coreClientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ClientResponse> createClient(@Valid @RequestBody CreateClientRequest request) {
        ClientResponse response = coreClientService.createClient(request);
        return ApiResponse.created(
                "CLIENT.CREATE_SUCCESS",
                "Thêm mới khách hàng thành công",
                response
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ClientResponse> getClientById(@PathVariable String id) {
        ClientResponse response = coreClientService.getClientById(id);
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
        List<ClientResponse> response = coreClientService.searchClients(keyword, trainerId);
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
        List<ClientResponse> response;
        if (search != null && !search.isBlank()) {
            response = coreClientService.searchClients(search, trainerId);
        } else {
            response = coreClientService.getAllClients(trainerId);
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
        ClientResponse response = coreClientService.updateClient(id, request);
        return ApiResponse.ok(
                "CLIENT.UPDATE_SUCCESS",
                "Cập nhật thông tin khách hàng thành công",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClient(@PathVariable String id) {
        coreClientService.deleteClient(id);
        return ApiResponse.ok(
                "CLIENT.DELETE_SUCCESS",
                "Xóa khách hàng thành công",
                null
        );
    }
}
