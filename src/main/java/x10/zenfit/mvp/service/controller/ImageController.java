package x10.zenfit.mvp.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x10.zenfit.mvp.service.common.domain.dto.request.ImageSignatureRequest;
import x10.zenfit.mvp.service.common.domain.dto.respone.ImageSignatureResponse;
import x10.zenfit.mvp.service.service.imageService.IImageSignatureService;
import x10.zenfit.mvp.service.share.response.ApiResponse;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageSignatureService imageSignatureService;

    @PostMapping("/signature")
    public ApiResponse<ImageSignatureResponse> generateSignature(@RequestBody ImageSignatureRequest request) {
        ImageSignatureResponse response = imageSignatureService.generateSignature(request);
        return ApiResponse.ok(
                "IMAGE.GENERATE_SIGNATURE_SUCCESS",
                "Tạo chữ ký upload ảnh thành công",
                response
        );
    }
}
