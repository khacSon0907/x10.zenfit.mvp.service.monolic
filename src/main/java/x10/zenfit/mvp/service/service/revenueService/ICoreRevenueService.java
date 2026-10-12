package x10.zenfit.mvp.service.service.revenueService;

import java.time.LocalDate;
import java.util.List;

public interface ICoreRevenueService {

    RevenueResponse getDailyRevenue(LocalDate date);

    RevenueResponse getWeeklyRevenue(LocalDate date);

    RevenueResponse getMonthlyRevenue(LocalDate date);

    RevenueResponse getTotalRevenue();

    List<TopSellingProductResponse> getTopSellingProducts(Integer limit);

    RevenueTargetResponse saveMonthlyTarget(RevenueTargetRequest request, String adminId);

    List<RevenueTargetResponse> getRevenueTargets();

    RevenueTargetResponse getRevenueTargetById(String id);

    RevenueTargetResponse updateRevenueTarget(String id, RevenueTargetRequest request);

    void deleteRevenueTarget(String id);

    RevenueTargetProgressResponse getTargetProgress(Integer year, Integer month);
}
