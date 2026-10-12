package x10.zenfit.mvp.service.service.refundService.approveRefundUc;

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
import x10.zenfit.mvp.service.common.domain.enums.RefundStatus;
import x10.zenfit.mvp.service.common.domain.enums.RefundType;
import x10.zenfit.mvp.service.service.refundService.RefundDecisionRequest;
import x10.zenfit.mvp.service.service.refundService.RefundResponse;
import x10.zenfit.mvp.service.service.refundService.RefundResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;
import x10.zenfit.mvp.service.share.exception.refund.RefundError;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApproveRefundUcImpl implements IApproveRefundUc {

    private final IRefundRepository refundRepository;
    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final IProductRepository productRepository;

    @Override
    public RefundResponse execute(String refundId, RefundDecisionRequest request) {
        RefundEntity refund = findRefundOrThrow(refundId);
        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Chi co the duyet yeu cau dang PENDING");
        }

        OrderEntity order = orderRepository.findById(refund.getOrderId())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));
        PaymentEntity payment = paymentRepository.findById(refund.getPaymentId())
                .orElseGet(() -> paymentRepository.findByOrderId(order.getId())
                        .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_ALLOWED, "Khong tim thay payment cua don hang")));

        LocalDateTime now = LocalDateTime.now();
        RefundStatus nextStatus = resolveApprovedStatus(refund);
        refund.setStatus(nextStatus);
        refund.setAdminNote(request == null ? null : request.getNote());
        refund.setProviderResponseCode(nextStatus == RefundStatus.EXCHANGE_APPROVED ? "EXCHANGE_SIZE_APPROVED" : "MANUAL_REFUND_REQUIRED");
        refund.setProviderResponseMessage(resolveApprovedMessage(nextStatus));
        refund.setApprovedAt(now);
        refund.setUpdatedAt(now);

        order.setStatus(nextStatus == RefundStatus.EXCHANGE_APPROVED ? OrderStatus.EXCHANGED : OrderStatus.RETURNED);
        order.setUpdatedAt(now);
        orderRepository.save(order);

        return RefundResponseMapper.toResponse(refundRepository.save(refund));
    }

    private RefundEntity findRefundOrThrow(String refundId) {
        return refundRepository.findById(refundId)
                .orElseThrow(() -> new BusinessException(RefundError.REFUND_NOT_FOUND));
    }

    private RefundStatus resolveApprovedStatus(RefundEntity refund) {
        if (refund.getType() != RefundType.EXCHANGE_SIZE) {
            return RefundStatus.APPROVED;
        }

        SizeVariantEntity requestedSize = findRequestedSize(refund);
        if (requestedSize.getQuantity() == null || requestedSize.getQuantity() <= 0) {
            return RefundStatus.REFUND_REQUIRED;
        }

        return RefundStatus.EXCHANGE_APPROVED;
    }

    private String resolveApprovedMessage(RefundStatus status) {
        if (status == RefundStatus.EXCHANGE_APPROVED) {
            return "Admin da duyet doi size. Khach gui hang ve shop de kiem tra va shop gui lai size moi.";
        }
        if (status == RefundStatus.REFUND_REQUIRED) {
            return "Size khach muon doi da het hang. Sau khi nhan va kiem tra hang tra ve, admin hoan tien thu cong.";
        }
        return "Admin da duyet tra hang. Sau khi nhan va kiem tra hang tra ve, admin hoan tien thu cong.";
    }

    private SizeVariantEntity findRequestedSize(RefundEntity refund) {
        ProductEntity product = productRepository.findById(refund.getProductId())
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay san pham can doi"));

        ColorVariantEntity color = product.getColors().stream()
                .filter(item -> refund.getColorId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay mau san pham can doi"));

        return color.getSizes().stream()
                .filter(size -> refund.getRequestedSizeId().equals(size.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(RefundError.INVALID_REFUND_DATA, "Khong tim thay size can doi"));
    }
}
