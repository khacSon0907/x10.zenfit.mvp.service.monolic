package x10.zenfit.mvp.service.common.domain.enums;

import lombok.Getter;

@Getter
public enum CancelReasonType {
    CUSTOMER_CHANGED_MIND("Khach doi y khong muon mua nua"),
    WRONG_PRODUCT("Khach dat nham san pham/mau/size"),
    WRONG_ADDRESS_OR_PHONE("Khach nhap sai dia chi hoac so dien thoai"),
    DUPLICATE_ORDER("Khach dat trung don hang"),
    PAYMENT_PROBLEM("Khach gap van de thanh toan"),
    DELIVERY_TOO_LONG("Thoi gian giao hang qua lau"),
    FOUND_BETTER_PRICE("Khach tim duoc gia tot hon"),
    OUT_OF_STOCK("Cua hang het hang"),
    ADMIN_CANCELLED("Nhan vien/cua hang huy don"),
    OTHER("Ly do khac");

    private final String label;

    CancelReasonType(String label) {
        this.label = label;
    }
}
