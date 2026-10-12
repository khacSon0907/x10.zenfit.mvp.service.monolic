package x10.zenfit.mvp.service.service.revenueService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.common.domain.entities.revenue.RevenueTargetEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.infrastructure.revenue.adapter.RevenueTargetMapper;
import x10.zenfit.mvp.service.infrastructure.revenue.db.mongodb.RevenueTargetDocument;
import x10.zenfit.mvp.service.infrastructure.revenue.db.mongodb.RevenueTargetMongoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreRevenueServiceImpl implements ICoreRevenueService {

    private final IOrderRepository orderRepository;
    private final RevenueTargetMongoRepository revenueTargetRepository;

    @Override
    public RevenueResponse getDailyRevenue(LocalDate date) {
        LocalDate targetDate = defaultToday(date);
        return buildRevenueResponse(
                "DAILY",
                targetDate,
                targetDate,
                findConfirmedOrdersBetween(targetDate, targetDate)
        );
    }

    @Override
    public RevenueResponse getWeeklyRevenue(LocalDate date) {
        LocalDate targetDate = defaultToday(date);
        LocalDate startDate = targetDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = targetDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return buildRevenueResponse(
                "WEEKLY",
                startDate,
                endDate,
                findConfirmedOrdersBetween(startDate, endDate)
        );
    }

    @Override
    public RevenueResponse getMonthlyRevenue(LocalDate date) {
        LocalDate targetDate = defaultToday(date);
        LocalDate startDate = targetDate.withDayOfMonth(1);
        LocalDate endDate = targetDate.withDayOfMonth(targetDate.lengthOfMonth());

        return buildRevenueResponse(
                "MONTHLY",
                startDate,
                endDate,
                findConfirmedOrdersBetween(startDate, endDate)
        );
    }

    @Override
    public RevenueResponse getTotalRevenue() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.COMPLETED);

        return buildRevenueResponse(
                "TOTAL",
                null,
                null,
                orders
        );
    }

    @Override
    public List<TopSellingProductResponse> getTopSellingProducts(Integer limit) {
        int safeLimit = normalizeLimit(limit);
        Map<String, ProductSalesSummary> summaries = new LinkedHashMap<>();

        orderRepository.findByStatus(OrderStatus.COMPLETED).stream()
                .filter(order -> order.getItems() != null)
                .flatMap(order -> order.getItems().stream())
                .filter(item -> item.getProductId() != null && !item.getProductId().isBlank())
                .forEach(item -> {
                    ProductSalesSummary summary = summaries.computeIfAbsent(
                            item.getProductId(),
                            productId -> new ProductSalesSummary(
                                    productId,
                                    item.getProductName(),
                                    item.getProductImage()
                            )
                    );

                    summary.add(item);
                });

        return summaries.values().stream()
                .sorted(Comparator
                        .comparingLong(ProductSalesSummary::getTotalQuantitySold)
                        .reversed()
                        .thenComparing(ProductSalesSummary::getTotalRevenue, Comparator.reverseOrder()))
                .limit(safeLimit)
                .map(ProductSalesSummary::toResponse)
                .toList();
    }

    @Override
    public RevenueTargetResponse saveMonthlyTarget(RevenueTargetRequest request, String adminId) {
        LocalDateTime now = LocalDateTime.now();
        RevenueTargetDocument target = revenueTargetRepository
                .findByYearAndMonth(request.getYear(), request.getMonth())
                .orElseGet(() -> RevenueTargetDocument.builder()
                        .id(UUID.randomUUID().toString())
                        .year(request.getYear())
                        .month(request.getMonth())
                        .createdBy(adminId)
                        .createdAt(now)
                        .build());

        target.setTargetRevenue(request.getTargetRevenue());
        target.setTargetOrders(request.getTargetOrders());
        target.setNote(normalize(request.getNote()));
        target.setUpdatedAt(now);

        RevenueTargetEntity saved = RevenueTargetMapper.toEntity(revenueTargetRepository.save(target));
        return toTargetResponse(saved);
    }

    @Override
    public List<RevenueTargetResponse> getRevenueTargets() {
        return revenueTargetRepository.findAllByOrderByYearDescMonthDesc().stream()
                .map(RevenueTargetMapper::toEntity)
                .map(this::toTargetResponse)
                .toList();
    }

    @Override
    public RevenueTargetResponse getRevenueTargetById(String id) {
        return revenueTargetRepository.findById(id)
                .map(RevenueTargetMapper::toEntity)
                .map(this::toTargetResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay target doanh thu"
                ));
    }

    @Override
    public RevenueTargetResponse updateRevenueTarget(String id, RevenueTargetRequest request) {
        RevenueTargetDocument target = revenueTargetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Khong tim thay target doanh thu"
                ));

        revenueTargetRepository.findByYearAndMonth(request.getYear(), request.getMonth())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Target doanh thu cua thang nay da ton tai"
                    );
                });

        target.setYear(request.getYear());
        target.setMonth(request.getMonth());
        target.setTargetRevenue(request.getTargetRevenue());
        target.setTargetOrders(request.getTargetOrders());
        target.setNote(normalize(request.getNote()));
        target.setUpdatedAt(LocalDateTime.now());

        RevenueTargetEntity saved = RevenueTargetMapper.toEntity(revenueTargetRepository.save(target));
        return toTargetResponse(saved);
    }

    @Override
    public void deleteRevenueTarget(String id) {
        if (!revenueTargetRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Khong tim thay target doanh thu"
            );
        }

        revenueTargetRepository.deleteById(id);
    }

    @Override
    public RevenueTargetProgressResponse getTargetProgress(Integer year, Integer month) {
        YearMonth targetMonth = resolveYearMonth(year, month);
        RevenueTargetEntity target = revenueTargetRepository
                .findByYearAndMonth(targetMonth.getYear(), targetMonth.getMonthValue())
                .map(RevenueTargetMapper::toEntity)
                .orElse(null);

        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.atEndOfMonth();
        List<OrderEntity> orders = findConfirmedOrdersBetween(startDate, endDate);
        BigDecimal currentRevenue = calculateRevenue(orders);
        long currentOrders = orders.size();

        BigDecimal targetRevenue = target == null || target.getTargetRevenue() == null
                ? BigDecimal.ZERO
                : target.getTargetRevenue();
        long targetOrders = target == null || target.getTargetOrders() == null
                ? 0
                : target.getTargetOrders();

        return RevenueTargetProgressResponse.builder()
                .target(toTargetResponse(target))
                .startDate(startDate)
                .endDate(endDate)
                .currentRevenue(currentRevenue)
                .currentOrders(currentOrders)
                .remainingRevenue(targetRevenue.subtract(currentRevenue).max(BigDecimal.ZERO))
                .remainingOrders(Math.max(targetOrders - currentOrders, 0))
                .revenueProgressPercent(calculatePercent(currentRevenue, targetRevenue))
                .orderProgressPercent(calculatePercent(BigDecimal.valueOf(currentOrders), BigDecimal.valueOf(targetOrders)))
                .revenueTargetAchieved(targetRevenue.compareTo(BigDecimal.ZERO) > 0
                        && currentRevenue.compareTo(targetRevenue) >= 0)
                .orderTargetAchieved(targetOrders > 0 && currentOrders >= targetOrders)
                .build();
    }

    private List<OrderEntity> findConfirmedOrdersBetween(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return orderRepository.findByStatusAndCreatedAtBetween(
                OrderStatus.COMPLETED,
                startDateTime,
                endDateTime
        );
    }

    private RevenueResponse buildRevenueResponse(String type, LocalDate startDate, LocalDate endDate, List<OrderEntity> orders) {
        BigDecimal totalRevenue = calculateRevenue(orders);

        return RevenueResponse.builder()
                .type(type)
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .totalOrders(orders.size())
                .build();
    }

    private LocalDate defaultToday(LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return 10;
        }

        return Math.min(limit, 100);
    }

    private BigDecimal calculateRevenue(List<OrderEntity> orders) {
        return orders.stream()
                .map(OrderEntity::getTotalAmount)
                .filter(totalAmount -> totalAmount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private YearMonth resolveYearMonth(Integer year, Integer month) {
        LocalDate today = LocalDate.now();
        int safeYear = year == null ? today.getYear() : year;
        int safeMonth = month == null ? today.getMonthValue() : month;
        return YearMonth.of(safeYear, safeMonth);
    }

    private BigDecimal calculatePercent(BigDecimal current, BigDecimal target) {
        if (target == null || target.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return current
                .multiply(BigDecimal.valueOf(100))
                .divide(target, 2, RoundingMode.HALF_UP);
    }

    private RevenueTargetResponse toTargetResponse(RevenueTargetEntity target) {
        if (target == null) {
            return null;
        }

        YearMonth targetMonth = YearMonth.of(target.getYear(), target.getMonth());
        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.atEndOfMonth();
        List<OrderEntity> orders = findConfirmedOrdersBetween(startDate, endDate);
        BigDecimal currentRevenue = calculateRevenue(orders);
        long currentOrders = orders.size();
        BigDecimal targetRevenue = target.getTargetRevenue() == null ? BigDecimal.ZERO : target.getTargetRevenue();
        long targetOrders = target.getTargetOrders() == null ? 0 : target.getTargetOrders();

        return RevenueTargetResponse.builder()
                .id(target.getId())
                .year(target.getYear())
                .month(target.getMonth())
                .targetRevenue(target.getTargetRevenue())
                .targetOrders(target.getTargetOrders())
                .note(target.getNote())
                .createdBy(target.getCreatedBy())
                .createdAt(target.getCreatedAt())
                .updatedAt(target.getUpdatedAt())
                .startDate(startDate)
                .endDate(endDate)
                .currentRevenue(currentRevenue)
                .currentOrders(currentOrders)
                .remainingRevenue(targetRevenue.subtract(currentRevenue).max(BigDecimal.ZERO))
                .remainingOrders(Math.max(targetOrders - currentOrders, 0))
                .revenueProgressPercent(calculatePercent(currentRevenue, targetRevenue))
                .orderProgressPercent(calculatePercent(BigDecimal.valueOf(currentOrders), BigDecimal.valueOf(targetOrders)))
                .revenueTargetAchieved(targetRevenue.compareTo(BigDecimal.ZERO) > 0
                        && currentRevenue.compareTo(targetRevenue) >= 0)
                .orderTargetAchieved(targetOrders > 0 && currentOrders >= targetOrders)
                .build();
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static class ProductSalesSummary {
        private final String productId;
        private String productName;
        private String productImage;
        private long totalQuantitySold;
        private BigDecimal totalRevenue = BigDecimal.ZERO;

        private ProductSalesSummary(String productId, String productName, String productImage) {
            this.productId = productId;
            this.productName = productName;
            this.productImage = productImage;
        }

        private void add(OrderItem item) {
            int quantity = item.getQuantity() == null ? 0 : item.getQuantity();
            BigDecimal itemRevenue = item.getTotalPrice() == null ? BigDecimal.ZERO : item.getTotalPrice();

            if (productName == null || productName.isBlank()) {
                productName = item.getProductName();
            }

            if (productImage == null || productImage.isBlank()) {
                productImage = item.getProductImage();
            }

            totalQuantitySold += quantity;
            totalRevenue = totalRevenue.add(itemRevenue);
        }

        private long getTotalQuantitySold() {
            return totalQuantitySold;
        }

        private BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        private TopSellingProductResponse toResponse() {
            return TopSellingProductResponse.builder()
                    .productId(productId)
                    .productName(productName)
                    .productImage(productImage)
                    .totalQuantitySold(totalQuantitySold)
                    .totalRevenue(totalRevenue)
                    .build();
        }
    }
}
