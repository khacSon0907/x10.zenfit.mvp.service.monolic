package x10.zenfit.mvp.service.service.imageService;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.dto.request.ImageSignatureRequest;
import x10.zenfit.mvp.service.common.domain.dto.respone.ImageSignatureResponse;
import x10.zenfit.mvp.service.common.domain.enums.ImageTargetType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageSignatureService implements IImageSignatureService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    public ImageSignatureService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public ImageSignatureResponse generateSignature(ImageSignatureRequest request) {
        if (request == null || request.getTargetType() == null) {
            throw new IllegalArgumentException("Image targetType is required");
        }

        Long timestamp = Instant.now().getEpochSecond();

        String folder = resolveFolder(request.getTargetType());

        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", folder);

        String signature = cloudinary.apiSignRequest(
                paramsToSign,
                cloudinary.config.apiSecret
        );

        return new ImageSignatureResponse(
                cloudName,
                apiKey,
                timestamp,
                signature,
                folder
        );
    }

    private String resolveFolder(ImageTargetType targetType) {
        return switch (targetType) {
            case PRODUCT -> "Zenfit/products";
            case USER_AVATAR -> "Zenfit/users/avatars";
            case CLIENT_AVATAR -> "Zenfit/clients/avatars";
            case CATEGORY -> "Zenfit/categories";
            case BANNER -> "Zenfit/banners";
            case REVIEW -> "Zenfit/reviews";
            case REFUND -> "Zenfit/refunds";
            case CHAT -> "Zenfit/chats";
            default -> throw new IllegalArgumentException("Unsupported image targetType: " + targetType);
        };
    }
}