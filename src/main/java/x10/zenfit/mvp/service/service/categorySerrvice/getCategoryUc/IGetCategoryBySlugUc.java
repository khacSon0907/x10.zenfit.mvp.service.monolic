package x10.zenfit.mvp.service.service.categorySerrvice.getCategoryUc;

import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryResp;

public interface IGetCategoryBySlugUc {
    CreateCategoryResp getBySlug(String slug);
}

