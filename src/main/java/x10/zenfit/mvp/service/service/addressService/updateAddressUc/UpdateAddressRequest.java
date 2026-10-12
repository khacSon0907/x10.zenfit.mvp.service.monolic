package x10.zenfit.mvp.service.service.addressService.updateAddressUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateAddressRequest {

    @NotBlank(message = "Tên người nhận không được để trống")
    private String receiverName;

    @NotBlank(message = "Số điện thoại nhận hàng không được để trống")
    @Pattern(regexp = "^(0|\\+84)[35789][0-9]{8}$", message = "Số điện thoại không đúng định dạng")
    private String receiverPhone;

    @NotBlank(message = "Mã tỉnh/thành phố không được để trống")
    private String provinceCode;

    @NotBlank(message = "Mã quận/huyện không được để trống")
    private String districtCode;

    @NotBlank(message = "Mã phường/xã không được để trống")
    private String wardCode;

    @NotBlank(message = "Tên tỉnh/thành phố không được để trống")
    private String provinceName;

    @NotBlank(message = "Tên quận/huyện không được để trống")
    private String districtName;

    @NotBlank(message = "Tên phường/xã không được để trống")
    private String wardName;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String streetAddress;

    private boolean isDefault;
}
