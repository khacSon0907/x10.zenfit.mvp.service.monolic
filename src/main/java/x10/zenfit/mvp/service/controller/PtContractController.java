
package x10.zenfit.mvp.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;
import x10.zenfit.mvp.service.service.contractService.ICorePtContractService;
import x10.zenfit.mvp.service.service.contractService.createPtContractUc.CreatePtContractRequest;
import x10.zenfit.mvp.service.service.contractService.updatePtContractUc.UpdatePtContractRequest;
import x10.zenfit.mvp.service.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/pt-contracts")
@RequiredArgsConstructor
public class PtContractController {

    private final ICorePtContractService corePtContractService;

    // Tạo hợp đồng PT
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PtContractEntity> createPtContract(
            @Valid @RequestBody CreatePtContractRequest request
    ) {
        PtContractEntity response =
                corePtContractService.createPtContract(request);

        return ApiResponse.created(
                "CONTRACT.CREATE_SUCCESS",
                "Tạo hợp đồng PT thành công",
                response
        );
    }

    // Lấy chi tiết hợp đồng theo ID
    @GetMapping("/{id}")
    public ApiResponse<PtContractEntity> getPtContractById(
            @PathVariable String id
    ) {
        PtContractEntity response =
                corePtContractService.getPtContractById(id);

        return ApiResponse.ok(
                "CONTRACT.GET_DETAIL_SUCCESS",
                "Lấy thông tin chi tiết hợp đồng PT thành công",
                response
        );
    }

    // Lấy tất cả hợp đồng theo trainerId
    @GetMapping("/trainer/{trainerId}")
    public ApiResponse<List<PtContractEntity>> getPtContractsByTrainerId(
            @PathVariable String trainerId
    ) {
        List<PtContractEntity> response =
                corePtContractService.getAllPtContractsByTrainerId(trainerId);

        return ApiResponse.ok(
                "CONTRACT.GET_BY_TRAINER_SUCCESS",
                "Lấy danh sách hợp đồng của PT thành công",
                response
        );
    }


    // Lấy danh sách hợp đồng và hỗ trợ lọc
    @GetMapping
    public ApiResponse<List<PtContractEntity>> getAllPtContracts(
            @RequestParam(required = false) String trainerId,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(required = false) PaymentContractStatus paymentStatus
    ) {
        List<PtContractEntity> response =
                corePtContractService.getAllPtContracts(
                        trainerId,
                        clientId,
                        status,
                        paymentStatus
                );

        return ApiResponse.ok(
                "CONTRACT.GET_ALL_SUCCESS",
                "Lấy danh sách hợp đồng PT thành công",
                response
        );
    }

    // Cập nhật hợp đồng PT
    @PutMapping("/{id}")
    public ApiResponse<PtContractEntity> updatePtContract(
            @PathVariable String id,
            @Valid @RequestBody UpdatePtContractRequest request
    ) {
        PtContractEntity response =
                corePtContractService.updatePtContract(id, request);

        return ApiResponse.ok(
                "CONTRACT.UPDATE_SUCCESS",
                "Cập nhật hợp đồng PT thành công",
                response
        );
    }




    // Xóa hợp đồng PT
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePtContract(
            @PathVariable String id
    ) {
        corePtContractService.deletePtContract(id);

        return ApiResponse.ok(
                "CONTRACT.DELETE_SUCCESS",
                "Xóa hợp đồng PT thành công",
                null
        );
    }
}
