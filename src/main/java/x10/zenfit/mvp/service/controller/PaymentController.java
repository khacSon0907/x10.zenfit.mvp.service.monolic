package x10.zenfit.mvp.service.controller;


// Thêm

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.zenfit.mvp.service.service.paymentService.ICorePaymentService;
import x10.zenfit.mvp.service.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.zenfit.mvp.service.service.paymentService.createPayment.CreatePaymentLinkResponse;
import x10.zenfit.mvp.service.share.response.ApiResponse;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ICorePaymentService paymentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CreatePaymentLinkResponse> createPaymentLink(
            @Valid @RequestBody CreatePaymentLinkRequest request
    ) {
        CreatePaymentLinkResponse response =
                paymentService.createPaymentLink(request);

        return ApiResponse.ok(
                "PAYMENT.CREATE_SUCCESS",
                "Tạo link thanh toán thành công",
                response
        );
    }
}