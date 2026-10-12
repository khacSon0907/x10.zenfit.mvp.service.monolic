package x10.zenfit.mvp.service.service.bannerService.createBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.BannerEntity;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBannerImpl implements ICreateBannerUc {

    private final IBannerRepository bannerRepository;

    @Override
    public CreateBannerResp createBanner(CreateBannerReq req) {

        BannerEntity entity = BannerEntity.builder()
                .id(UUID.randomUUID().toString())
                .title(normalize(req.getTitle()))
                .subtitle(req.getSubtitle())
                .imageUrl(req.getImageUrl())
                .linkUrl(req.getLinkUrl())
                .active(req.getActive() == null || req.getActive())
                .sortOrder(req.getSortOrder() == null ? 0 : req.getSortOrder())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        BannerEntity saved = bannerRepository.save(entity);

        return toResp(saved);
    }

    static CreateBannerResp toResp(BannerEntity e) {
        return CreateBannerResp.builder()
                .id(e.getId())
                .title(e.getTitle())
                .subtitle(e.getSubtitle())
                .imageUrl(e.getImageUrl())
                .linkUrl(e.getLinkUrl())
                .active(e.getActive())
                .sortOrder(e.getSortOrder())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
