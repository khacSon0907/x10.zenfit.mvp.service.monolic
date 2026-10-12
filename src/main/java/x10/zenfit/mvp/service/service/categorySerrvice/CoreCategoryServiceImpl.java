package x10.zenfit.mvp.service.service.categorySerrvice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryReq;
import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.CreateCategoryResp;
import x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc.ICreateCategoryUc;
import x10.zenfit.mvp.service.service.categorySerrvice.deleteCategoryUc.IDeleteCategoryUc;
import x10.zenfit.mvp.service.service.categorySerrvice.getCategoryUc.IGetCategoriesUc;
import x10.zenfit.mvp.service.service.categorySerrvice.getCategoryUc.IGetCategoryBySlugUc;
import x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc.IUpdateCategoryUc;
import x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc.UpdateCategoryReq;
import x10.zenfit.mvp.service.service.categorySerrvice.updateCategoryUc.UpdateCategoryResp;

import java.util.List;

@Service
@AllArgsConstructor
public class CoreCategoryServiceImpl implements ICoreCategoryService {

    private final ICreateCategoryUc createCategoryUc;
    private final IGetCategoriesUc getCategoriesUc;
    private final IGetCategoryBySlugUc getCategoryBySlugUc;
    private final IUpdateCategoryUc updateCategoryUc;
    private final IDeleteCategoryUc deleteCategoryUc;

    @Override
    public CreateCategoryResp createCategory(CreateCategoryReq req) {
        return createCategoryUc.createCategory(req);
    }

    @Override
    public List<CreateCategoryResp> getAll() {
        return getCategoriesUc.getAll();
    }

    @Override
    public CreateCategoryResp getBySlug(String slug) {
        return getCategoryBySlugUc.getBySlug(slug);
    }

    @Override
    public UpdateCategoryResp updateCategory(UpdateCategoryReq req) {
        return updateCategoryUc.updateCategory(req);
    }

    @Override
    public void deleteCategory(String categoryId) {
        deleteCategoryUc.deleteCategory(categoryId);
    }
}
