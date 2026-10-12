package x10.zenfit.mvp.service.service.voucherService;

import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.Repository.IVoucherRepository;
import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;
import x10.zenfit.mvp.service.common.domain.enums.VoucherDiscountType;
import x10.zenfit.mvp.service.common.domain.enums.VoucherSource;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.voucher.VoucherError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoreVoucherServiceImplTest {

    @Test
    void createVoucherRequiresStartAtAndEndAt() {
        CoreVoucherServiceImpl service = newService(mock(IVoucherRepository.class));
        CreateVoucherRequest request = validCreateRequest();
        request.setStartAt(null);

        assertThatThrownBy(() -> service.createVoucher(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(error -> assertThat(((BusinessException) error).getError())
                        .isEqualTo(VoucherError.VOUCHER_INVALID));
    }

    @Test
    void createVoucherRejectsEndAtBeforeStartAt() {
        CoreVoucherServiceImpl service = newService(mock(IVoucherRepository.class));
        CreateVoucherRequest request = validCreateRequest();
        request.setEndAt(request.getStartAt().minusMinutes(1));

        assertThatThrownBy(() -> service.createVoucher(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(error -> assertThat(((BusinessException) error).getError())
                        .isEqualTo(VoucherError.VOUCHER_INVALID));
    }

    @Test
    void getVoucherReturnsVoucherDetail() {
        IVoucherRepository voucherRepository = mock(IVoucherRepository.class);
        CoreVoucherServiceImpl service = newService(voucherRepository);
        VoucherEntity voucher = validVoucher();

        when(voucherRepository.findById("voucher-1")).thenReturn(Optional.of(voucher));

        VoucherResponse response = service.getVoucher("voucher-1");

        assertThat(response.getId()).isEqualTo("voucher-1");
        assertThat(response.getCode()).isEqualTo("SALE10");
        assertThat(response.getStartAt()).isEqualTo(voucher.getStartAt());
        assertThat(response.getEndAt()).isEqualTo(voucher.getEndAt());
    }

    @Test
    void updateVoucherUpdatesQuantityAndActiveTimeWindow() {
        IVoucherRepository voucherRepository = mock(IVoucherRepository.class);
        CoreVoucherServiceImpl service = newService(voucherRepository);
        VoucherEntity voucher = validVoucher();
        LocalDateTime newStartAt = LocalDateTime.now().plusDays(2);
        LocalDateTime newEndAt = LocalDateTime.now().plusDays(8);
        UpdateVoucherRequest request = new UpdateVoucherRequest();
        request.setName("Tet sale");
        request.setDescription("Voucher Tet");
        request.setDiscountType(VoucherDiscountType.FIXED_AMOUNT);
        request.setDiscountValue(BigDecimal.valueOf(50_000));
        request.setMinOrderAmount(BigDecimal.valueOf(200_000));
        request.setMaxDiscountAmount(BigDecimal.valueOf(50_000));
        request.setUsageLimit(20);
        request.setActive(false);
        request.setStartAt(newStartAt);
        request.setEndAt(newEndAt);

        when(voucherRepository.findById("voucher-1")).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(any(VoucherEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VoucherResponse response = service.updateVoucher("voucher-1", request);

        assertThat(response.getName()).isEqualTo("Tet sale");
        assertThat(response.getDescription()).isEqualTo("Voucher Tet");
        assertThat(response.getDiscountType()).isEqualTo(VoucherDiscountType.FIXED_AMOUNT);
        assertThat(response.getDiscountValue()).isEqualByComparingTo("50000");
        assertThat(response.getMinOrderAmount()).isEqualByComparingTo("200000");
        assertThat(response.getMaxDiscountAmount()).isEqualByComparingTo("50000");
        assertThat(response.getUsageLimit()).isEqualTo(20);
        assertThat(response.isActive()).isFalse();
        assertThat(response.getStartAt()).isEqualTo(newStartAt);
        assertThat(response.getEndAt()).isEqualTo(newEndAt);
    }

    @Test
    void applyVoucherCalculatesDiscountForCheckout() {
        IVoucherRepository voucherRepository = mock(IVoucherRepository.class);
        CoreVoucherServiceImpl service = newService(voucherRepository);
        VoucherEntity voucher = validVoucher();

        when(voucherRepository.findByCode("SALE10")).thenReturn(Optional.of(voucher));

        ApplyVoucherRequest request = new ApplyVoucherRequest();
        request.setCode("SALE10");
        request.setSubtotal(BigDecimal.valueOf(500_000));

        ApplyVoucherResponse response = service.applyVoucher(request);

        assertThat(response.getDiscountAmount()).isEqualByComparingTo("50000");
        assertThat(response.getTotalAfterDiscount()).isEqualByComparingTo("450000");
    }

    @Test
    void getAvailableVouchersReturnsOnlyUsableVouchersForCheckout() {
        IVoucherRepository voucherRepository = mock(IVoucherRepository.class);
        CoreVoucherServiceImpl service = newService(voucherRepository);
        VoucherEntity publicVoucher = validVoucher();
        VoucherEntity userVoucher = validVoucher();
        userVoucher.setId("voucher-2");
        userVoucher.setCode("USER20");
        userVoucher.setAssignedUserId("user-1");
        VoucherEntity otherUserVoucher = validVoucher();
        otherUserVoucher.setId("voucher-3");
        otherUserVoucher.setCode("OTHER20");
        otherUserVoucher.setAssignedUserId("user-2");
        VoucherEntity expiredVoucher = validVoucher();
        expiredVoucher.setId("voucher-4");
        expiredVoucher.setCode("OLD10");
        expiredVoucher.setEndAt(LocalDateTime.now().minusMinutes(1));
        VoucherEntity minOrderVoucher = validVoucher();
        minOrderVoucher.setId("voucher-5");
        minOrderVoucher.setCode("MIN999");
        minOrderVoucher.setMinOrderAmount(BigDecimal.valueOf(999_000));

        when(voucherRepository.findAll()).thenReturn(List.of(
                publicVoucher,
                userVoucher,
                otherUserVoucher,
                expiredVoucher,
                minOrderVoucher
        ));

        List<VoucherResponse> responses = service.getAvailableVouchers("user-1", BigDecimal.valueOf(500_000));

        assertThat(responses).extracting(VoucherResponse::getCode)
                .containsExactly("SALE10", "USER20");
    }

    private CoreVoucherServiceImpl newService(IVoucherRepository voucherRepository) {
        return new CoreVoucherServiceImpl(voucherRepository, mock(IUserRepository.class));
    }

    private CreateVoucherRequest validCreateRequest() {
        CreateVoucherRequest request = new CreateVoucherRequest();
        request.setCode("SALE10");
        request.setName("Sale 10%");
        request.setDiscountType(VoucherDiscountType.PERCENTAGE);
        request.setDiscountValue(BigDecimal.TEN);
        request.setMinOrderAmount(BigDecimal.ZERO);
        request.setUsageLimit(10);
        request.setSource(VoucherSource.MANUAL);
        request.setActive(true);
        request.setStartAt(LocalDateTime.now().minusDays(1));
        request.setEndAt(LocalDateTime.now().plusDays(7));
        return request;
    }

    private VoucherEntity validVoucher() {
        return VoucherEntity.builder()
                .id("voucher-1")
                .code("SALE10")
                .name("Sale 10%")
                .discountType(VoucherDiscountType.PERCENTAGE)
                .discountValue(BigDecimal.TEN)
                .minOrderAmount(BigDecimal.ZERO)
                .usageLimit(10)
                .usedCount(0)
                .source(VoucherSource.MANUAL)
                .active(true)
                .startAt(LocalDateTime.now().minusDays(1))
                .endAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();
    }
}
