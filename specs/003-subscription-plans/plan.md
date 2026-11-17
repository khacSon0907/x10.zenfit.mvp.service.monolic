# Implementation Plan: Admin Subscription Plan Management & PT Student Tier Limits

**Branch**: `003-subscription-plans` | **Date**: 2026-11-17 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/003-subscription-plans/spec.md`

## Summary

Xây dựng bộ API CRUD toàn diện cho Quản trị viên (Admin) quản lý các Gói Dịch Vụ (`SubscriptionPlanEntity`) trên nền tảng ZenFit:
- **Quản lý hạn mức & tính phí**: Admin thiết lập các gói dịch vụ với giá tiền (`price`), thời hạn (`durationDays`), giới hạn học viên (`maxStudents`), và giới hạn buổi tập (`maxTrainingSessions`).
- **Gói miễn phí mặc định (`isDefault = true`)**: Quy định số lượng học viên tối đa mà PT được phép quản lý miễn phí (do Admin chỉnh sửa và cấu hình). Hệ thống đảm bảo tại mọi thời điểm chỉ có DUY NHẤT một gói giữ trạng thái mặc định; khi kích hoạt gói mặc định mới, gói cũ tự động gỡ cờ.
- **Vòng đời & Trạng thái gói**: Kiểm soát chặt chẽ trạng thái (`ACTIVE`, `INACTIVE`, `ARCHIVED`). Ngăn chặn xóa hoặc vô hiệu hóa gói mặc định.
- **Phân quyền chặt chẽ**: Quyền ghi (Tạo, Sửa, Đổi trạng thái, Xóa, Xem danh sách quản trị phân trang) dành riêng cho `ROLE_ADMIN`; PT và học viên có quyền xem danh sách các gói công khai (`ACTIVE`) sắp xếp theo `displayOrder`.
- **Tuân thủ chuẩn kiến trúc ZenFit Constitution**: Tách biệt rõ ràng Domain Entity vs Persistence Document, phân rã Use Case theo Interface, bọc toàn bộ phản hồi qua `ApiResponse<T>`, quản lý lỗi tập trung qua `BusinessException`.

---

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 3.5.0 (Spring Web, Spring Security, Spring Data MongoDB, Lombok, Jakarta Validation)

**Storage**: MongoDB (collection: `subscription_plans`)

**Testing**: JUnit 5, Spring Boot Test, Mockito

**Target Platform**: Linux / Windows JVM runtime (Monolithic Web Service)

**Project Type**: Monolithic Web REST API (`x10.zenfit.mvp.service`)

**Performance Goals**: < 100ms thời gian phản hồi cho truy vấn danh sách gói đang hoạt động; < 300ms cho các thao tác CRUD và phân trang quản trị.

**Constraints**:
- Sử dụng đúng Domain Entity `SubscriptionPlanEntity` và Domain Enum `SubscriptionPlanStatus` đã có sẵn tại `common.domain`.
- Mã gói (`code`) phải là duy nhất trên toàn hệ thống (không phân biệt chữ hoa chữ thường).
- Đảm bảo tính duy nhất của cờ `isDefault = true` (không có 2 gói mặc định đồng thời).
- Không được phép xóa gói mặc định (`isDefault = true`).
- Mọi phản hồi bọc qua `ApiResponse<T>`, quản lý lỗi qua `BusinessException` và `SubscriptionPlanError`.

**Scale/Scope**: Quản lý gói dịch vụ và hạn mức học viên cho toàn bộ PT và quản lý trên ZenFit.

---

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Nguyên tắc / Quy tắc | Trạng thái | Đánh giá & Kiểm chứng |
| :--- | :--- | :--- |
| **I. Layering & Unidirectional Dependency** | **PASS** | Luồng dữ liệu 1 chiều: `SubscriptionPlanController -> ICoreSubscriptionPlanService / Specific Use Cases -> ISubscriptionPlanRepository -> SubscriptionPlanRepositoryImpl -> MongoTemplate / SubscriptionPlanDocument`. Controller không truy cập DB. |
| **II. Entity vs Document Separation** | **PASS** | `SubscriptionPlanEntity` thuộc `common.domain.entities.subscription` thuần túy POJO không chứa annotation DB. `SubscriptionPlanDocument` nằm trong `infrastructure.subscription.db.mongodb`. Ánh xạ 2 chiều thông qua `SubscriptionPlanMapper`. |
| **III. Interface-Driven Use Case** | **PASS** | Nghiệp vụ chia nhỏ thành các Use Case chuyên trách (`createPlanUc`, `getPlansUc`, `getPlanUc`, `updatePlanUc`, `updatePlanStatusUc`, `deletePlanUc`) với interface và class gắn `@Component`. `CoreSubscriptionPlanServiceImpl` đóng vai trò Facade điều phối. |
| **IV. Zero Secret Exposure** | **PASS** | Không sử dụng hay can thiệp vào bí mật hệ thống. Phân quyền Admin được kiểm soát qua `SecurityContext` và cấu hình bảo mật `SecurityConfig`. |
| **V. Uniform API Response Envelope** | **PASS** | Mọi API đều trả về `ApiResponse<T>`. Lỗi nghiệp vụ định nghĩa tập trung trong `SubscriptionPlanError` kế thừa `ErrorDescriptor`, ném qua `BusinessException`. |

---

## Project Structure

### Documentation (this feature)

```text
specs/003-subscription-plans/
├── spec.md              # Feature specification & requirements
├── plan.md              # Implementation Plan (this file)
├── research.md          # Phase 0: Technical decisions & research
├── data-model.md        # Phase 1: Data structures, DTOs & state transitions
├── quickstart.md        # Phase 1: End-to-end curl validation guide
└── contracts/           # Phase 1: OpenAPI contract definition
    └── subscription-plan-api.json
```

### Source Code (repository root)

```text
src/main/java/x10/zenfit/mvp/service/
├── common/domain/
│   ├── entities/subscription/
│   │   └── SubscriptionPlanEntity.java                       # Domain Entity (đã có sẵn)
│   └── enums/
│       └── SubscriptionPlanStatus.java                       # Domain Enum (đã có sẵn: ACTIVE, INACTIVE, ARCHIVED)
├── share/exception/subscription/
│   └── SubscriptionPlanError.java                            # ErrorDescriptor cho gói dịch vụ
├── Repository/
│   └── ISubscriptionPlanRepository.java                      # Interface trừu tượng tầng dữ liệu
├── infrastructure/subscription/
│   ├── db/mongodb/
│   │   ├── SubscriptionPlanDocument.java                     # MongoDB Document (@Document)
│   │   └── SubscriptionPlanMongoRepository.java              # Spring Data Mongo Repository
│   └── adapter/
│       ├── SubscriptionPlanRepositoryImpl.java               # Triển khai ISubscriptionPlanRepository
│       └── mapper/
│           └── SubscriptionPlanMapper.java                   # Ánh xạ Entity <-> Document
├── service/subscriptionPlanService/
│   ├── ICoreSubscriptionPlanService.java                     # Facade Interface
│   ├── CoreSubscriptionPlanServiceImpl.java                  # Facade Implementation
│   ├── SubscriptionPlanResponse.java                         # Response DTO dùng chung
│   ├── createPlanUc/                                         # Use case tạo gói
│   │   ├── ICreateSubscriptionPlanUc.java
│   │   ├── CreateSubscriptionPlanUcImpl.java
│   │   └── CreateSubscriptionPlanRequest.java
│   ├── getPlansUc/                                           # Use case lấy danh sách active & admin phân trang
│   │   ├── IGetSubscriptionPlansUc.java
│   │   └── GetSubscriptionPlansUcImpl.java
│   ├── getPlanUc/                                            # Use case lấy chi tiết gói & gói mặc định
│   │   ├── IGetSubscriptionPlanUc.java
│   │   └── GetSubscriptionPlanUcImpl.java
│   ├── updatePlanUc/                                         # Use case cập nhật thông tin gói
│   │   ├── IUpdateSubscriptionPlanUc.java
│   │   ├── UpdateSubscriptionPlanUcImpl.java
│   │   └── UpdateSubscriptionPlanRequest.java
│   ├── updatePlanStatusUc/                                   # Use case đổi trạng thái gói
│   │   ├── IUpdateSubscriptionPlanStatusUc.java
│   │   ├── UpdateSubscriptionPlanStatusUcImpl.java
│   │   └── UpdateSubscriptionPlanStatusRequest.java
│   └── deletePlanUc/                                         # Use case xóa gói
│       ├── IDeleteSubscriptionPlanUc.java
│       └── DeleteSubscriptionPlanUcImpl.java
├── controller/
│   └── SubscriptionPlanController.java                       # REST Controller: /api/subscription-plans
└── config/security/
    └── SecurityConfig.java                                   # Cập nhật phân quyền /api/subscription-plans/**
```

---

## Complexity Tracking

> **Constitution Check: 100% PASS - Không có vi phạm kiến trúc nào.**

| Violation | Why Needed | Simpler Alternative Rejected Because |
| :--- | :--- | :--- |
| Không có | Tuân thủ tuyệt đối quy tắc phân tầng, tách biệt entity/document, use case encapsulation | Không áp dụng |
