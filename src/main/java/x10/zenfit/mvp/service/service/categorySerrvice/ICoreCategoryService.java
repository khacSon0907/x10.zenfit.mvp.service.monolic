package x10.zenfit.mvp.service.service.categorySerrvice;

import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryReq;
import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryResp;
import x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc.UpdateCategoryReq;
import x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc.UpdateCategoryResp;

import java.util.List;

public interface ICoreCategoryService {
    CreateCategoryResp createCategory(CreateCategoryReq req);
    List<CreateCategoryResp> getAll();
    CreateCategoryResp getBySlug(String slug);
    UpdateCategoryResp updateCategory(UpdateCategoryReq req);
    void deleteCategory(String categoryId);
}
