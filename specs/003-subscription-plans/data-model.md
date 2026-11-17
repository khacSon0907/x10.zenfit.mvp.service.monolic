# Phase 1: Data Model & Contracts - Subscription Plans Management

**Feature**: Admin CRUD Subscription Plans for PT & Client Limits  
**Date**: 2026-11-17  
**Branch**: `003-subscription-plans`

---

## 1. Domain Entities & Database Mapping

### 1.1. Domain Entity (`SubscriptionPlanEntity`)

Đại diện cho thực thể nghiệp vụ Gói dịch vụ trong bộ nhớ hệ thống (không chứa annotation của Spring Data MongoDB):

```text
Package: x10.zenfit.mvp.service.common.domain.entities.subscription
Class: SubscriptionPlanEntity
```

| Tên trường | Kiểu dữ liệu | Mô tả | Ràng buộc nghiệp vụ |
| :--- | :--- | :--- | :--- |
| `id` | `String` | Khóa chính của gói | Định danh hệ thống MongoDB ObjectId |
| `code` | `String` | Mã định danh gói (slug) | Duy nhất trên toàn hệ thống (VD: `FREE_PT`, `PRO_1M`) |
| `name` | `String` | Tên hiển thị của gói | Bắt buộc, không để trống (VD: `Gói Miễn Phí Khởi Nghiệp`) |
| `description` | `String` | Mô tả chi tiết gói | Tùy chọn, giải thích quyền lợi gói |
| `price` | `BigDecimal` | Giá tiền của gói (VND) | >= 0 (Gói miễn phí = 0) |
| `durationDays` | `Integer` | Chu kỳ sử dụng (ngày) | >= 1 (VD: 30 ngày, 365 ngày) |
| `maxStudents` | `Integer` | Giới hạn học viên được quản lý | >= 0 (Quy định hạn mức học viên cho PT) |
| `maxTrainingSessions`| `Integer` | Giới hạn buổi tập (mở rộng) | Tùy chọn, >= 0 |
| `status` | `SubscriptionPlanStatus` | Trạng thái gói | `ACTIVE`, `INACTIVE`, `ARCHIVED` |
| `isDefault` | `Boolean` | Cờ gói mặc định miễn phí | Duy nhất 1 gói trong DB có `isDefault = true` |
| `displayOrder` | `Integer` | Thứ tự ưu tiên hiển thị | Số nguyên, sắp xếp tăng dần |
| `createdAt` | `LocalDateTime` | Thời điểm tạo | Tự động gán thời gian máy chủ |
| `updatedAt` | `LocalDateTime` | Thời điểm cập nhật cuối | Cập nhật mỗi lần chỉnh sửa |

---

### 1.2. Persistence Document (`SubscriptionPlanDocument`)

Đại diện cho cấu trúc tài liệu được lưu trong MongoDB collection `subscription_plans`:

```text
Package: x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb
Class: SubscriptionPlanDocument
Collection: subscription_plans
```

```java
@Document(collection = "subscription_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String name;
    private String description;

    private BigDecimal price;
    private Integer durationDays;

    private Integer maxStudents;
    private Integer maxTrainingSessions;

    private SubscriptionPlanStatus status;

    @Indexed
    private Boolean isDefault;

    private Integer displayOrder;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 2. Request / Response Data Transfer Objects (DTOs)

### 2.1. `CreateSubscriptionPlanRequest`
Payload khi Admin tạo gói mới:

```java
public class CreateSubscriptionPlanRequest {
    @NotBlank(message = "Mã gói không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9_-]{2,30}$", message = "Mã gói chỉ gồm 2-30 ký tự chữ cái, chữ số, gạch nối hoặc gạch dưới")
    private String code;

    @NotBlank(message = "Tên gói không được để trống")
    @Size(max = 100, message = "Tên gói tối đa 100 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @NotNull(message = "Giá gói không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá gói không được âm")
    private BigDecimal price;

    @NotNull(message = "Chu kỳ sử dụng không được để trống")
    @Min(value = 1, message = "Chu kỳ sử dụng tối thiểu 1 ngày")
    private Integer durationDays;

    @NotNull(message = "Giới hạn học viên không được để trống")
    @Min(value = 0, message = "Giới hạn học viên không được âm")
    private Integer maxStudents;

    @Min(value = 0, message = "Giới hạn buổi tập không được âm")
    private Integer maxTrainingSessions;

    private SubscriptionPlanStatus status; // Mặc định ACTIVE nếu null

    private Boolean isDefault; // Mặc định false nếu null

    private Integer displayOrder; // Mặc định 0 nếu null
}
```

### 2.2. `UpdateSubscriptionPlanRequest`
Payload khi Admin cập nhật thông tin gói (không cho đổi `code` để tránh phá vỡ liên kết):

```java
public class UpdateSubscriptionPlanRequest {
    @NotBlank(message = "Tên gói không được để trống")
    @Size(max = 100, message = "Tên gói tối đa 100 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @NotNull(message = "Giá gói không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá gói không được âm")
    private BigDecimal price;

    @NotNull(message = "Chu kỳ sử dụng không được để trống")
    @Min(value = 1, message = "Chu kỳ sử dụng tối thiểu 1 ngày")
    private Integer durationDays;

    @NotNull(message = "Giới hạn học viên không được để trống")
    @Min(value = 0, message = "Giới hạn học viên không được âm")
    private Integer maxStudents;

    @Min(value = 0, message = "Giới hạn buổi tập không được âm")
    private Integer maxTrainingSessions;

    private Boolean isDefault;

    private Integer displayOrder;
}
```

### 2.3. `UpdateSubscriptionPlanStatusRequest`
Payload khi Admin chuyển đổi trạng thái:

```java
public class UpdateSubscriptionPlanStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private SubscriptionPlanStatus status;
}
```

### 2.4. `SubscriptionPlanResponse`
DTO phản hồi chuẩn trả về cho client:

```java
public class SubscriptionPlanResponse {
    private String id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDays;
    private Integer maxStudents;
    private Integer maxTrainingSessions;
    private SubscriptionPlanStatus status;
    private Boolean isDefault;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 3. State Machine & Transition Rules

```
       [Tạo mới]
           │
           ▼
     ┌───────────┐
     │  ACTIVE   │ ◄──────────┐
     └─────┬─────┘            │
           │                  │ (Mở lại)
 (Ẩn tạm)  │                  │
           ▼                  │
     ┌───────────┐            │
     │ INACTIVE  │ ───────────┘
     └─────┬─────┘
           │
           │ (Ngừng vĩnh viễn)
           ▼
     ┌───────────┐
     │ ARCHIVED  │ (Chỉ đọc, không cho sửa/kích hoạt lại)
     └───────────┘
```

- **Quy tắc chuyển trạng thái**:
  - `ACTIVE` -> `INACTIVE`: Cho phép ẩn gói khỏi màn hình mua gói của PT.
  - `INACTIVE` -> `ACTIVE`: Cho phép mở lại gói nếu gói chưa bị lưu trữ.
  - `ACTIVE`/`INACTIVE` -> `ARCHIVED`: Đánh dấu gói lưu trữ lịch sử, không thể quay lại `ACTIVE` và không thể sửa đổi nội dung.
  - Gói đang là `isDefault = true` **KHÔNG ĐƯỢC PHÉP** chuyển sang `INACTIVE` hoặc `ARCHIVED`.

---

## 4. Error Descriptors (`SubscriptionPlanError`)

Được khai báo trong package `x10.zenfit.mvp.service.share.exception.subscription.SubscriptionPlanError`:

| Mã lỗi (Enum) | Mã HTTP | Error Code Chuỗi | Thông điệp giải thích |
| :--- | :--- | :--- | :--- |
| `PLAN_NOT_FOUND` | 404 NOT_FOUND | `SUB_PLAN_404` | Không tìm thấy gói dịch vụ với ID yêu cầu |
| `CODE_ALREADY_EXISTS` | 409 CONFLICT | `SUB_PLAN_409` | Mã gói dịch vụ đã tồn tại trong hệ thống |
| `CANNOT_DELETE_DEFAULT_PLAN` | 400 BAD_REQUEST | `SUB_PLAN_400_DEFAULT` | Không thể xóa hoặc vô hiệu hóa gói dịch vụ mặc định |
| `CANNOT_MODIFY_ARCHIVED_PLAN`| 400 BAD_REQUEST | `SUB_PLAN_400_ARCHIVED` | Không thể chỉnh sửa hoặc kích hoạt gói đã lưu trữ |
| `INVALID_PLAN_DATA` | 400 BAD_REQUEST | `SUB_PLAN_400_INVALID` | Dữ liệu cấu hình gói không hợp lệ |

