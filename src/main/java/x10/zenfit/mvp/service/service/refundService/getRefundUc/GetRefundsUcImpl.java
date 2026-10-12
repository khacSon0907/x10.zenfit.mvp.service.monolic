package x10.zenfit.mvp.service.service.refundService.getRefundUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IRefundRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.RefundEntity;
import x10.zenfit.mvp.service.service.refundService.RefundResponse;
import x10.zenfit.mvp.service.service.refundService.RefundResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.refund.RefundError;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRefundsUcImpl implements IGetRefundsUc {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;
    private static final String CURSOR_SEPARATOR = "|";

    private final IRefundRepository refundRepository;

    @Override
    public List<RefundResponse> getMyRefunds(String userId) {
        return refundRepository.findByUserId(userId).stream()
                .map(RefundResponseMapper::toResponse)
                .toList();
    }

    @Override
    public List<RefundResponse> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(RefundResponseMapper::toResponse)
                .toList();
    }

    @Override
    public RefundCursorPageResponse getAllRefundsByCursor(String cursor, Integer limit) {
        int pageSize = normalizeLimit(limit);
        CursorValue cursorValue = decodeCursor(cursor);

        List<RefundEntity> refunds = refundRepository.findAllByCursor(
                cursorValue.createdAt(),
                cursorValue.id(),
                pageSize + 1
        );

        boolean hasNext = refunds.size() > pageSize;
        List<RefundEntity> pageItems = hasNext ? refunds.subList(0, pageSize) : refunds;
        RefundEntity lastItem = pageItems.isEmpty() ? null : pageItems.get(pageItems.size() - 1);

        return RefundCursorPageResponse.builder()
                .items(pageItems.stream()
                        .map(RefundResponseMapper::toResponse)
                        .toList())
                .nextCursor(hasNext ? encodeCursor(lastItem) : null)
                .hasNext(hasNext)
                .limit(pageSize)
                .build();
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit < 1) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }

    private CursorValue decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return new CursorValue(null, null);
        }

        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );
            String[] parts = decoded.split("\\" + CURSOR_SEPARATOR, 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new IllegalArgumentException("Invalid cursor");
            }

            return new CursorValue(LocalDateTime.parse(parts[0]), parts[1]);
        } catch (Exception e) {
            throw new BusinessException(RefundError.INVALID_REFUND_DATA, "Cursor phan trang khong hop le");
        }
    }

    private String encodeCursor(RefundEntity refund) {
        String rawCursor = refund.getCreatedAt() + CURSOR_SEPARATOR + refund.getId();
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(rawCursor.getBytes(StandardCharsets.UTF_8));
    }

    private record CursorValue(LocalDateTime createdAt, String id) {
    }
}
