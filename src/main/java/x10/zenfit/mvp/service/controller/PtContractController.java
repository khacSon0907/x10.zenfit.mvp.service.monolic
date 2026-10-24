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
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.contractService.ICorePtContractService;
import x10.zenfit.mvp.service.service.contractService.PtContractResponse;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.CreatePtContractRequest;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.UpdatePtContractRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/pt-contracts")
@RequiredArgsConstructor
public class PtContractController {

    private final ICorePtContractService corePtContractService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PtContractResponse> createPtContract(@Valid @RequestBody CreatePtContractRequest request) {
        PtContractResponse response = corePtContractService.createPtContract(request);
        return ApiResponse.created(
                "CONTRACT.CREATE_SUCCESS",
                "Tạo hợp đồng PT thành công",
                response
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<PtContractResponse> getPtContractById(@PathVariable String id) {
        PtContractResponse response = corePtContractService.getPtContractById(id);
        return ApiResponse.ok(
                "CONTRACT.GET_DETAIL_SUCCESS",
                "Lấy thông tin chi tiết hợp đồng PT thành công",
                response
        );
    }

    @GetMapping
    public ApiResponse<List<PtContractResponse>> getAllPtContracts(
            @RequestParam(required = false) String trainerId,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) ContractStatus status
    ) {
        List<PtContractResponse> response = corePtContractService.getAllPtContracts(trainerId, clientId, status);
        return ApiResponse.ok(
                "CONTRACT.GET_ALL_SUCCESS",
                "Lấy danh sách hợp đồng PT thành công",
                response
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<PtContractResponse> updatePtContract(
            @PathVariable String id,
            @Valid @RequestBody UpdatePtContractRequest request
    ) {
        PtContractResponse response = corePtContractService.updatePtContract(id, request);
        return ApiResponse.ok(
                "CONTRACT.UPDATE_SUCCESS",
                "Cập nhật hợp đồng PT thành công",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePtContract(@PathVariable String id) {
        corePtContractService.deletePtContract(id);
        return ApiResponse.ok(
                "CONTRACT.DELETE_SUCCESS",
                "Xóa hợp đồng PT thành công",
                null
        );
    }
}
