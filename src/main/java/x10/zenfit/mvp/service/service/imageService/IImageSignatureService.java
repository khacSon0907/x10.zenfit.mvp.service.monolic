package x10.zenfit.mvp.service.service.imageService;

import x10.zenfit.mvp.service.common.domain.dto.request.ImageSignatureRequest;
import x10.zenfit.mvp.service.common.domain.dto.respone.ImageSignatureResponse;

public interface IImageSignatureService {

    ImageSignatureResponse generateSignature(ImageSignatureRequest request);
}