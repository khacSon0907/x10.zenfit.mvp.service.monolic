# Phase 0: Research & Technical Decisions - Subscription Plans Management

**Feature**: Admin CRUD Subscription Plans for PT & Client Limits  
**Date**: 2026-11-17  
**Branch**: `003-subscription-plans`

---

## 1. Research Objectives

Nghiên cứu và đưa ra các quyết định kỹ thuật nhằm hiện thực hóa module Quản lý gói dịch vụ (Subscription Plan), đáp ứng yêu cầu kinh doanh:
- Admin tự do định nghĩa các gói dịch vụ, giá tiền, thời hạn và số lượng học viên tối đa cho PT/Quản lý.
- Đảm bảo gói miễn phí (`isDefault = true`) luôn tồn tại và duy nhất, từ đó Admin có thể dễ dàng cấu hình "mỗi PT được lưu bao nhiêu học viên miễn phí".
- Đảm bảo tính tuân thủ tuyệt đối theo 5 nguyên tắc cốt lõi của **ZenFit Backend Constitution**.

---

## 2. Technical Decisions & Rationales

### Decision 1: Đảm bảo tính duy nhất của gói mặc định (`isDefault = true`)

- **Bối cảnh**: Hệ thống cần 1 gói dịch vụ mặc định (thường là Free, giá = 0, ví dụ cho phép quản lý tối đa 5 học viên). Khi PT đăng ký tài khoản, hệ thống sẽ tự động gán gói mặc định này. Khi Admin tạo hoặc sửa một gói thành `isDefault = true`, toàn bộ các gói khác phải tự động chuyển thành `isDefault = false`.
- **Quyết định chọn**:
  - Tại tầng Application / Use Case: Khi lưu hoặc cập nhật một gói có `isDefault = true`, `ISubscriptionPlanRepository` sẽ cung cấp phương thức `clearDefaultFlagExcept(String currentPlanId)`.
  - Triển khai trong `SubscriptionPlanRepositoryImpl` thông qua `MongoTemplate.updateMulti(...)` để cập nhật `isDefault = false` cho mọi tài liệu khác có `isDefault = true`.
- **Lý do chọn**: Đảm bảo tính nhất quán dữ liệu nguyên tử mà không gây văng ngoại lệ `DuplicateKeyException` khó hiểu cho người dùng khi Admin muốn chuyển giao quyền gói mặc định từ gói A sang gói B.
- **Phương án khác đã xem xét**:
  - *MongoDB Partial Unique Index*: Tạo partial unique index `{ isDefault: 1 }` với `partialFilterExpression: { isDefault: true }`. Bị loại vì khi Admin muốn đổi gói B làm default, MongoDB sẽ ném lỗi trùng lặp trừ khi Admin phải làm 2 bước thủ công (bỏ default gói A trước rồi mới set default gói B).

---

### Decision 2: Kiểm tra tính duy nhất của mã gói (`code`)

- **Bối cảnh**: `code` là định danh nghiệp vụ cố định (ví dụ: `FREE_PT`, `PRO_MONTHLY`, `VIP_ANNUAL`).
- **Quyết định chọn**:
  - Chuẩn hóa mã `code`: tự động `trim()` và chuyển thành chữ in hoa (`toUpperCase()`).
  - Tầng Domain/Service: Kiểm tra tồn tại `existsByCodeIgnoreCase(code)` trước khi insert/update, ném `BusinessException(SubscriptionPlanError.CODE_ALREADY_EXISTS)` khi vi phạm.
  - Tầng Database: Thêm `@Indexed(unique = true)` trên trường `code` trong `SubscriptionPlanDocument` để phòng ngừa race condition.
- **Lý do chọn**: Bắt lỗi sớm và trả về mã lỗi rõ ràng (`HTTP 409 Conflict`) cho Admin.

---

### Decision 3: Chiến lược Xóa & Quản lý vòng đời (Lifecycle)

- **Bối cảnh**: Tránh trường hợp xóa mất gói đang được sử dụng, đồng thời cung cấp khả năng ẩn/lưu trữ gói.
- **Quyết định chọn**:
  - Enum trạng thái `SubscriptionPlanStatus`:
    - `ACTIVE`: Gói đang mở bán / đang áp dụng công khai cho PT đăng ký.
    - `INACTIVE`: Tạm thời ẩn, không cho đăng ký mới nhưng người đang dùng vẫn có hiệu lực.
    - `ARCHIVED`: Gói đã ngừng kinh doanh vĩnh viễn, lưu trữ lịch sử.
  - Nguyên tắc khi gọi API `DELETE /api/subscription-plans/{id}`:
    - Nếu gói có `isDefault = true`: **CẤM XÓA** (ném `CANNOT_DELETE_DEFAULT_PLAN`).
    - Nếu gói có trạng thái `ACTIVE` hoặc đang liên kết với đăng ký của PT: từ chối xóa hoặc khuyến nghị chuyển sang `ARCHIVED`.
    - Cung cấp API chuyên dụng `PATCH /api/subscription-plans/{id}/status` để chuyển đổi trạng thái an toàn.

---

### Decision 4: Phân quyền truy cập (Authorization)

- **Bối cảnh**: Chỉ Admin mới được quyền cấu hình gói và định mức học viên; PT chỉ được xem danh sách gói công khai để lựa chọn.
- **Quyết định chọn**:
  - Phân quyền endpoint trong `SecurityConfig.java`:
    - `POST /api/subscription-plans/**` -> `hasRole("ADMIN")`
    - `PUT /api/subscription-plans/**` -> `hasRole("ADMIN")`
    - `PATCH /api/subscription-plans/**` -> `hasRole("ADMIN")`
    - `DELETE /api/subscription-plans/**` -> `hasRole("ADMIN")`
    - `GET /api/subscription-plans/admin/**` -> `hasRole("ADMIN")`
    - `GET /api/subscription-plans` -> `hasAnyRole("ADMIN", "HEAD_OF_TRAINING", "TEAM_MANAGER", "PT", "STAFF", "USER")` (hoặc `authenticated()` / `permitAll()`)
    - `GET /api/subscription-plans/{id}` -> `authenticated()`
- **Lý do chọn**: Tách bạch rõ ràng giữa màn hình cấu hình của Admin và màn hình xem gói nâng cấp của PT.

---

### Decision 5: Kiến trúc & Đóng gói Use Case theo Constitution

- **Bối cảnh**: Tuân thủ tuyệt đối Kiến trúc phân tầng 1 chiều và quy tắc phân rã Use Case độc lập.
- **Cấu trúc gói**:
  - **Domain Entity**: `x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity` (đã có sẵn, giữ nguyên vẹn).
  - **Domain Enum**: `x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus` (đã có sẵn, gồm `ACTIVE`, `INACTIVE`, `ARCHIVED`).
  - **Exception**: `x10.zenfit.mvp.service.share.exception.subscription.SubscriptionPlanError` (chứa các mã lỗi: `PLAN_NOT_FOUND`, `CODE_ALREADY_EXISTS`, `CANNOT_DELETE_DEFAULT_PLAN`, `INVALID_PLAN_DATA`, v.v.).
  - **Repository Interface**: `x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository`.
  - **Infrastructure MongoDB**:
    - `x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.SubscriptionPlanDocument`
    - `x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.SubscriptionPlanMongoRepository`
    - `x10.zenfit.mvp.service.infrastructure.subscription.adapter.SubscriptionPlanRepositoryImpl`
    - `x10.zenfit.mvp.service.infrastructure.subscription.adapter.mapper.SubscriptionPlanMapper`
  - **Application Service & Use Cases**:
    - `service.subscriptionPlanService.ICoreSubscriptionPlanService`
    - `service.subscriptionPlanService.CoreSubscriptionPlanServiceImpl`
    - `service.subscriptionPlanService.createPlanUc.*`
    - `service.subscriptionPlanService.getPlansUc.*`
    - `service.subscriptionPlanService.getPlanUc.*`
    - `service.subscriptionPlanService.updatePlanUc.*`
    - `service.subscriptionPlanService.updatePlanStatusUc.*`
    - `service.subscriptionPlanService.deletePlanUc.*`
  - **Controller**:
    - `x10.zenfit.mvp.service.controller.SubscriptionPlanController`

