package x10.zenfit.mvp.service.service.categorySerrvice;

import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;
import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryResp;
import x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc.UpdateCategoryResp;

import java.util.List;

public final class CategoryResponseMapper {

    private CategoryResponseMapper() {
    }

    public static CreateCategoryResp toCreateResp(CategoryEntity category) {
        return toCreateResp(category, List.of());
    }

    public static CreateCategoryResp toCreateResp(CategoryEntity category, List<CreateCategoryResp> children) {
        return CreateCategoryResp.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .active(category.isActive())
                .bannerUrl(category.getBannerUrl())
                .parentId(category.getParentId())
                .sortOrder(category.getSortOrder())
                .children(children)
                .build();
    }

    public static UpdateCategoryResp toUpdateResp(CategoryEntity category) {
        return UpdateCategoryResp.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .active(category.isActive())
                .bannerUrl(category.getBannerUrl())
                .parentId(category.getParentId())
                .sortOrder(category.getSortOrder())
                .build();
    }
}
