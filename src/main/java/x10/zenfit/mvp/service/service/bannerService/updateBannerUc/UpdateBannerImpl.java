package x10.zenfit.mvp.service.service.bannerService.updateBannerUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IBannerRepository;
import x10.zenfit.mvp.service.common.domain.entities.banner.BannerEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.banner.BannerError;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdateBannerImpl implements IUpdateBannerUc {

    private final IBannerRepository bannerRepository;

    @Override
    public UpdateBannerResp updateBanner(UpdateBannerReq req) {

        BannerEntity existing = bannerRepository.findById(req.getId())
                .orElseThrow(() -> new BusinessException(BannerError.BANNER_NOT_FOUND));

        // Chỉ cập nhật các field được cung cấp (partial update)
        if (req.getTitle() != null && !req.getTitle().isBlank()) {
            existing.setTitle(req.getTitle().trim());
        }
        if (req.getSubtitle() != null) {
            existing.setSubtitle(req.getSubtitle());
        }
        if (req.getImageUrl() != null && !req.getImageUrl().isBlank()) {
            existing.setImageUrl(req.getImageUrl());
        }
        if (req.getLinkUrl() != null) {
            existing.setLinkUrl(req.getLinkUrl());
        }
        if (req.getActive() != null) {
            existing.setActive(req.getActive());
        }
        if (req.getSortOrder() != null) {
            existing.setSortOrder(req.getSortOrder());
        }

        existing.setUpdatedAt(Instant.now());

        BannerEntity saved = bannerRepository.save(existing);

        return UpdateBannerResp.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .subtitle(saved.getSubtitle())
                .imageUrl(saved.getImageUrl())
                .linkUrl(saved.getLinkUrl())
                .active(saved.getActive())
                .sortOrder(saved.getSortOrder())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}
