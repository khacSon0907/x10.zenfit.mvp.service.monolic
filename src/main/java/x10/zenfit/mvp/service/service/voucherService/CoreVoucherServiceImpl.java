package x10.zenfit.mvp.service.service.voucherService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.Repository.IVoucherRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;
import x10.zenfit.mvp.service.common.domain.enums.VoucherDiscountType;
import x10.zenfit.mvp.service.common.domain.enums.VoucherSource;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;
import x10.zenfit.mvp.service.share.exception.voucher.VoucherError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreVoucherServiceImpl implements ICoreVoucherService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final String BIRTHDAY_EVENT_NAME_PREFIX = "BIRTHDAY-";

    private final IVoucherRepository voucherRepository;
    private final IUserRepository userRepository;

    @Override
    public VoucherResponse createVoucher(CreateVoucherRequest request) {
        LocalDateTime now = LocalDateTime.now();
        validateVoucherWindow(request.getStartAt(), request.getEndAt());
        String code = normalizeCode(request.getCode());
        if (voucherRepository.existsByCode(code)) {
            throw new BusinessException(VoucherError.VOUCHER_CODE_EXISTS);
        }

        VoucherEntity voucher = VoucherEntity.builder()
                .id(UUID.randomUUID().toString())
                .code(code)
                .name(request.getName())
                .description(request.getDescription())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minOrderAmount(defaultZero(request.getMinOrderAmount()))
                .maxDiscountAmount(request.getMaxDiscountAmount())
                .usageLimit(defaultUsageLimit(request.getUsageLimit()))
                .usedCount(0)
                .assignedUserId(blankToNull(request.getAssignedUserId()))
                .source(request.getSource() == null ? VoucherSource.MANUAL : request.getSource())
                .eventName(request.getEventName())
                .active(request.getActive() == null || request.getActive())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return VoucherResponseMapper.toResponse(voucherRepository.save(voucher));
    }

    @Override
    public List<VoucherResponse> issueEventVouchers(IssueEventVouchersRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return request.getUserIds().stream()
                .distinct()
                .map(userId -> createEventVoucherForUser(request, userId, now))
                .map(VoucherResponseMapper::toResponse)
                .toList();
    }

    @Override
    public VoucherResponse issueBirthdayVoucher(String userId, IssueBirthdayVoucherRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        if (user.getBirthday() == null) {
            throw new BusinessException(VoucherError.BIRTHDAY_MISSING);
        }
        if (!MonthDay.from(user.getBirthday()).equals(MonthDay.from(LocalDate.now()))) {
            throw new BusinessException(VoucherError.BIRTHDAY_NOT_TODAY);
        }

        LocalDateTime now = LocalDateTime.now();
        String eventName = BIRTHDAY_EVENT_NAME_PREFIX + now.getYear();
        if (voucherRepository.existsByAssignedUserIdAndSourceAndEventName(userId, VoucherSource.BIRTHDAY, eventName)) {
            return findIssuedVoucher(userId, VoucherSource.BIRTHDAY, eventName);
        }

        VoucherEntity voucher = VoucherEntity.builder()
                .id(UUID.randomUUID().toString())
                .code(uniqueCode("BDAY50-" + userId.substring(0, Math.min(6, userId.length()))))
                .name("Birthday voucher 50%")
                .description("Voucher sinh nhat giam 50% cho user")
                .discountType(VoucherDiscountType.PERCENTAGE)
                .discountValue(BigDecimal.valueOf(50))
                .minOrderAmount(BigDecimal.ZERO)
                .maxDiscountAmount(null)
                .usageLimit(1)
                .usedCount(0)
                .assignedUserId(userId)
                .source(VoucherSource.BIRTHDAY)
                .eventName(eventName)
                .active(true)
                .startAt(request == null ? now : request.getStartAt())
                .endAt(request == null || request.getEndAt() == null ? now.plusDays(30) : request.getEndAt())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return VoucherResponseMapper.toResponse(voucherRepository.save(voucher));
    }

    @Override
    public List<VoucherResponse> getAll() {
        return voucherRepository.findAll().stream().map(VoucherResponseMapper::toResponse).toList();
    }

    @Override
    public VoucherResponse getVoucher(String id) {
        VoucherEntity voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(VoucherError.VOUCHER_NOT_FOUND));
        return VoucherResponseMapper.toResponse(voucher);
    }

    @Override
    public List<VoucherResponse> getAvailableVouchers(String userId, BigDecimal subtotal) {
        BigDecimal safeSubtotal = defaultZero(subtotal);
        return voucherRepository.findAll().stream()
                .filter(voucher -> isAvailableForCheckout(voucher, userId, safeSubtotal))
                .map(VoucherResponseMapper::toResponse)
                .toList();
    }

    @Override
    public List<VoucherResponse> getMyVouchers(String userId) {
        return voucherRepository.findByAssignedUserId(userId).stream().map(VoucherResponseMapper::toResponse).toList();
    }

    @Override
    public VoucherResponse updateVoucher(String id, UpdateVoucherRequest request) {
        validateVoucherWindow(request.getStartAt(), request.getEndAt());
        VoucherEntity voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(VoucherError.VOUCHER_NOT_FOUND));

        voucher.setName(request.getName());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinOrderAmount(defaultZero(request.getMinOrderAmount()));
        voucher.setMaxDiscountAmount(request.getMaxDiscountAmount());
        voucher.setUsageLimit(defaultUsageLimit(request.getUsageLimit()));
        voucher.setActive(request.getActive() == null || request.getActive());
        voucher.setStartAt(request.getStartAt());
        voucher.setEndAt(request.getEndAt());
        voucher.setUpdatedAt(LocalDateTime.now());

        return VoucherResponseMapper.toResponse(voucherRepository.save(voucher));
    }

    @Override
    public ApplyVoucherResponse applyVoucher(ApplyVoucherRequest request) {
        VoucherCalculationResult result = validateAndCalculate(request.getCode(), request.getUserId(), request.getSubtotal());
        BigDecimal subtotal = defaultZero(request.getSubtotal());
        return ApplyVoucherResponse.builder()
                .voucherId(result.getVoucher().getId())
                .code(result.getVoucher().getCode())
                .discountAmount(result.getDiscountAmount())
                .subtotal(subtotal)
                .totalAfterDiscount(subtotal.subtract(result.getDiscountAmount()).max(BigDecimal.ZERO))
                .build();
    }

    @Override
    public VoucherCalculationResult validateAndCalculate(String code, String userId, BigDecimal subtotal) {
        VoucherEntity voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(VoucherError.VOUCHER_NOT_FOUND));
        validateVoucher(voucher, userId, subtotal);

        return VoucherCalculationResult.builder()
                .voucher(voucher)
                .discountAmount(calculateDiscount(voucher, subtotal))
                .build();
    }

    @Override
    public void markVoucherUsed(String voucherId) {
        if (voucherId == null || voucherId.isBlank()) {
            return;
        }

        VoucherEntity voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new BusinessException(VoucherError.VOUCHER_NOT_FOUND));
        voucher.setUsedCount(defaultInt(voucher.getUsedCount()) + 1);
        voucher.setUpdatedAt(LocalDateTime.now());
        voucherRepository.save(voucher);
    }

    @Override
    public void deleteVoucher(String id) {
        if (voucherRepository.findById(id).isEmpty()) {
            throw new BusinessException(VoucherError.VOUCHER_NOT_FOUND);
        }
        voucherRepository.deleteById(id);
    }

    private VoucherResponse findIssuedVoucher(String userId, VoucherSource source, String eventName) {
        return voucherRepository.findByAssignedUserId(userId).stream()
                .filter(voucher -> source == voucher.getSource())
                .filter(voucher -> eventName.equals(voucher.getEventName()))
                .findFirst()
                .map(VoucherResponseMapper::toResponse)
                .orElseThrow(() -> new BusinessException(VoucherError.VOUCHER_INVALID));
    }

    private VoucherEntity createEventVoucherForUser(IssueEventVouchersRequest request, String userId, LocalDateTime now) {
        validateVoucherWindow(request.getStartAt(), request.getEndAt());
        userRepository.findById(userId).orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        if (voucherRepository.existsByAssignedUserIdAndSourceAndEventName(userId, VoucherSource.EVENT, request.getEventName())) {
            return voucherRepository.findByAssignedUserId(userId).stream()
                    .filter(voucher -> VoucherSource.EVENT == voucher.getSource())
                    .filter(voucher -> request.getEventName().equals(voucher.getEventName()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(VoucherError.VOUCHER_INVALID));
        }

        VoucherEntity voucher = VoucherEntity.builder()
                .id(UUID.randomUUID().toString())
                .code(uniqueCode(request.getCodePrefix() + "-" + userId.substring(0, Math.min(6, userId.length()))))
                .name("Event voucher " + request.getEventName())
                .description("Voucher su kien " + request.getEventName())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minOrderAmount(defaultZero(request.getMinOrderAmount()))
                .maxDiscountAmount(request.getMaxDiscountAmount())
                .usageLimit(1)
                .usedCount(0)
                .assignedUserId(userId)
                .source(VoucherSource.EVENT)
                .eventName(request.getEventName())
                .active(true)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return voucherRepository.save(voucher);
    }

    private void validateVoucher(VoucherEntity voucher, String userId, BigDecimal subtotal) {
        LocalDateTime now = LocalDateTime.now();
        if (!voucher.isActive()) {
            throw new BusinessException(VoucherError.VOUCHER_INACTIVE);
        }
        if (voucher.getStartAt() != null && voucher.getStartAt().isAfter(now)) {
            throw new BusinessException(VoucherError.VOUCHER_NOT_STARTED);
        }
        if (voucher.getEndAt() != null && voucher.getEndAt().isBefore(now)) {
            throw new BusinessException(VoucherError.VOUCHER_EXPIRED);
        }
        if (voucher.getUsageLimit() != null && defaultInt(voucher.getUsedCount()) >= voucher.getUsageLimit()) {
            throw new BusinessException(VoucherError.VOUCHER_USAGE_LIMIT_REACHED);
        }
        if (voucher.getAssignedUserId() != null && !voucher.getAssignedUserId().equals(userId)) {
            throw new BusinessException(VoucherError.VOUCHER_USER_NOT_ALLOWED);
        }
        if (defaultZero(subtotal).compareTo(defaultZero(voucher.getMinOrderAmount())) < 0) {
            throw new BusinessException(VoucherError.VOUCHER_MIN_ORDER_NOT_MET);
        }
    }

    private boolean isAvailableForCheckout(VoucherEntity voucher, String userId, BigDecimal subtotal) {
        LocalDateTime now = LocalDateTime.now();
        if (voucher == null || !voucher.isActive()) {
            return false;
        }
        if (voucher.getStartAt() == null || voucher.getStartAt().isAfter(now)) {
            return false;
        }
        if (voucher.getEndAt() == null || voucher.getEndAt().isBefore(now)) {
            return false;
        }
        if (voucher.getUsageLimit() != null && defaultInt(voucher.getUsedCount()) >= voucher.getUsageLimit()) {
            return false;
        }
        if (voucher.getAssignedUserId() != null && !voucher.getAssignedUserId().equals(userId)) {
            return false;
        }
        return subtotal.compareTo(defaultZero(voucher.getMinOrderAmount())) >= 0;
    }

    private void validateVoucherWindow(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt == null || endAt == null) {
            throw new BusinessException(VoucherError.VOUCHER_INVALID, "Voucher phai co thoi gian bat dau va ket thuc");
        }
        if (!endAt.isAfter(startAt)) {
            throw new BusinessException(VoucherError.VOUCHER_INVALID, "Thoi gian ket thuc phai sau thoi gian bat dau");
        }
    }

    private BigDecimal calculateDiscount(VoucherEntity voucher, BigDecimal subtotal) {
        BigDecimal safeSubtotal = defaultZero(subtotal);
        BigDecimal discount = VoucherDiscountType.PERCENTAGE == voucher.getDiscountType()
                ? safeSubtotal.multiply(voucher.getDiscountValue()).divide(ONE_HUNDRED, 0, RoundingMode.HALF_UP)
                : voucher.getDiscountValue();

        if (voucher.getMaxDiscountAmount() != null && discount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
            discount = voucher.getMaxDiscountAmount();
        }

        return discount.min(safeSubtotal).max(BigDecimal.ZERO);
    }

    private String uniqueCode(String rawPrefix) {
        String prefix = normalizeCode(rawPrefix).replaceAll("[^A-Z0-9-]", "");
        String code;
        do {
            code = prefix + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        } while (voucherRepository.existsByCode(code));
        return code;
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Integer defaultUsageLimit(Integer value) {
        return value == null || value < 1 ? 1 : value;
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }
}
