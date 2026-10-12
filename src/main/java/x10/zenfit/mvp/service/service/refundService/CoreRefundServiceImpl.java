package x10.zenfit.mvp.service.service.refundService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IPaymentRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.Repository.IRefundRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.RefundEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;
import x10.zenfit.mvp.service.common.domain.enums.RefundStatus;
import x10.zenfit.mvp.service.common.domain.enums.RefundType;
import x10.zenfit.mvp.service.service.refundService.approveRefundUc.IApproveRefundUc;
import x10.zenfit.mvp.service.service.refundService.getRefundUc.IGetRefundsUc;
import x10.zenfit.mvp.service.service.refundService.getRefundUc.RefundCursorPageResponse;
import x10.zenfit.mvp.service.service.refundService.rejectRefundUc.IRejectRefundUc;
import x10.zenfit.mvp.service.service.refundService.requestRefundUc.IRequestRefundUc;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;
import x10.zenfit.mvp.service.share.exception.refund.RefundError;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreRefundServiceImpl implements ICoreRefundService {

    private final IRequestRefundUc requestRefundUc;
    private final IGetRefundsUc getRefundsUc;
    private final IApproveRefundUc approveRefundUc;
    private final IRejectRefundUc rejectRefundUc;
    private final IRefundRepository refundRepository;
    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final IProductRepository productRepository;

    @Override
    public RefundResponse requestRefund(String userId, RefundRequest request) {
        return requestRefundUc.execute(userId, request);
    }

    @Override
    public List<RefundResponse> getMyRefunds(String userId) {
        return getRefundsUc.getMyRefunds(userId);
    }

    @Override
    public List<RefundResponse> getAllRefunds() {
        return getRefundsUc.getAllRefunds();
    }

    @Override
    public RefundCursorPageResponse getAllRefundsByCursor(String cursor, Integer limit) {
        return getRefundsUc.getAllRefundsByCursor(cursor, limit);
    }

    @Override
    public RefundResponse approveRefund(String refundId, RefundDecisionRequest request) {
        return approveRefundUc.execute(refundId, request);
    }

    @Override
    public RefundResponse markReturnReceived(String refundId, RefundDecisionRequest request) {
        RefundEntity refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_FOUND));

        if (refund.getStatus() != RefundStatus.APPROVED
                && refund.getStatus() != RefundStatus.EXCHANGE_APPROVED
                && refund.getStatus() != RefundStatus.REFUND_REQUIRED) {
            throw new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Chi xac nhan nhan hang voi yeu cau da duyet");
        }

        OrderEntity order = orderRepository.findById(refund.getOrderId())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        refund.setReceivedAt(now);
        refund.setAdminNote(request == null || request.getNote() == null ? refund.getAdminNote() : request.getNote());

        if (refund.getType() == RefundType.EXCHANGE_SIZE && hasRequestedSizeInStock(refund)) {
            refund.setStatus(RefundStatus.EXCHANGED);
            refund.setProviderResponseCode("EXCHANGE_SIZE_COMPLETED");
            refund.setProviderResponseMessage("Admin da nhan hang tra ve hop le va xac nhan gui lai san pham cung mau voi size moi cho khach");
            refund.setProcessedAt(now);
            order.setStatus(OrderStatus.EXCHANGED);
        } else {
            refund.setStatus(RefundStatus.REFUND_REQUIRED);
            refund.setProviderResponseCode("MANUAL_REFUND_REQUIRED");
            refund.setProviderResponseMessage("Admin da nhan hang tra ve hop le. Hien thi thong tin ngan hang de admin hoan tien thu cong.");
            order.setStatus(OrderStatus.RETURNED);
        }

        refund.setUpdatedAt(now);
        order.setUpdatedAt(now);
        orderRepository.save(order);

        return RefundResponseMapper.toResponse(refundRepository.save(refund));
    }

    @Override
    public RefundResponse markRefunded(String refundId, RefundDecisionRequest request) {
        RefundEntity refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_FOUND));

        if (refund.getStatus() != RefundStatus.REFUND_REQUIRED
                && refund.getStatus() != RefundStatus.RETURN_RECEIVED
                && refund.getStatus() != RefundStatus.PROCESSING) {
            throw new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Chi co the xac nhan da hoan tien sau khi shop da nhan va kiem tra hang tra ve hop le");
        }

        OrderEntity order = orderRepository.findById(refund.getOrderId())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));
        PaymentEntity payment = paymentRepository.findById(refund.getPaymentId())
                .orElseGet(() -> paymentRepository.findByOrderId(order.getId())
                        .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Khong tim thay payment cua don hang")));

        LocalDateTime now = LocalDateTime.now();
        refund.setStatus(RefundStatus.SUCCESS);
        refund.setAdminNote(request == null || request.getNote() == null ? refund.getAdminNote() : request.getNote());
        refund.setProviderResponseCode("MANUAL_REFUNDED");
        refund.setProviderResponseMessage("Admin da xac nhan da chuyen khoan hoan tien thu cong");
        refund.setProcessedAt(now);
        refund.setUpdatedAt(now);

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(now);
        payment.setUpdatedAt(now);
        paymentRepository.save(payment);

        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order.setStatus(OrderStatus.REFUNDED);
        order.setUpdatedAt(now);
        orderRepository.save(order);

        return RefundResponseMapper.toResponse(refundRepository.save(refund));
    }

    @Override
    public RefundResponse rejectRefund(String refundId, RefundDecisionRequest request) {
        return rejectRefundUc.execute(refundId, request);
    }

    private boolean hasRequestedSizeInStock(RefundEntity refund) {
        if (refund.getRequestedSizeId() == null || refund.getRequestedSizeId().isBlank()) {
            return false;
        }

        ProductEntity product = productRepository.findById(refund.getProductId())
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay san pham can doi"));

        ColorVariantEntity color = product.getColors().stream()
                .filter(item -> refund.getColorId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay mau san pham can doi"));

        SizeVariantEntity requestedSize = color.getSizes().stream()
                .filter(size -> refund.getRequestedSizeId().equals(size.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay size can doi"));

        return requestedSize.getQuantity() != null && requestedSize.getQuantity() > 0;
    }
}
