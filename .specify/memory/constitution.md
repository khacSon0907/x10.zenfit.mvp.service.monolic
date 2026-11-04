<!--
Sync Impact Report:
- Version change: 0.0.0 -> 1.0.0 (Initial Ratification)
- Source: AGENTS.md distilled into concise, testable engineering principles
- Modified principles: Initialized 5 core principles
- Added sections:
  * Core Principles (Layering, Entity-Document Separation, Use Case Encapsulation, Zero Secret Leakage, Uniform API Responses)
  * Technical Stack & Architectural Constraints
  * Development & Verification Workflow
  * Governance
- Removed sections: None
- Follow-up TODOs: None
-->

# ZenFit Backend Constitution

## Core Principles

### I. Layering & Unidirectional Dependency (Kiến trúc phân tầng một chiều)
Mọi luồng dữ liệu bắt buộc tuân theo chiều: `Controller -> Application Service / Use Case -> Repository Interface -> Infrastructure Adapter -> MongoDB Document`.
- Controller KHÔNG được chứa logic nghiệp vụ, KHÔNG được inject hoặc truy cập trực tiếp Repository hoặc MongoDB Document.
- Controller CHỈ nhận request, validate DTO đầu vào qua `@Valid`, và chuyển tiếp sang Service/Use Case.
- Domain models KHÔNG được phụ thuộc ngược vào Controller, HTTP request DTO, hoặc MongoDB Document.
- Tuyệt đối không tạo phụ thuộc vòng (circular dependency) giữa các package.

### II. Domain Entity vs Persistence Document Separation (Tách biệt thực thể nghiệp vụ và tài liệu lưu trữ)
Dự án phân biệt rõ ràng giữa Domain Entity và Database Document:
- `Domain Entity` (trong `common.domain.entities`) đại diện cho khái niệm nghiệp vụ; KHÔNG chứa annotation Spring Data MongoDB (`@Document`, `@Id`, `@Field`).
- `Persistence Document` (trong `infrastructure...db.mongodb`) đại diện cho cấu trúc lưu trữ MongoDB; KHÔNG được truyền trực tiếp vào Use Case hoặc trả về qua Controller.
- Việc chuyển đổi giữa Domain Entity và Persistence Document BẮT BUỘC thực hiện qua Mapper chuyên trách trong `infrastructure...adapter.mapper`.

### III. Interface-Driven Use Case Encapsulation (Đóng gói Use Case theo giao diện)
Mọi nghiệp vụ ứng dụng phải được tổ chức thành các use case độc lập theo feature:
- Mỗi use case nằm trong thư mục riêng (ví dụ: `createGoalUc/`), định nghĩa interface (ví dụ: `ICreateClientGoalUc`) và class triển khai gắn `@Component`.
- Service chính (ví dụ: `ICoreClientGoalService` và `CoreClientGoalServiceImpl`) đóng vai trò facade điều phối các use case, không nhồi nhét toàn bộ logic vào một class khổng lồ.
- Không đưa logic nghiệp vụ hoặc truy vấn cơ sở dữ liệu vào Configuration hoặc Utility classes.

### IV. Zero Secret Exposure & Security Hygiene (Bảo mật thông tin và quản lý cấu hình)
An toàn thông tin là nguyên tắc tuyệt đối không thể thỏa hiệp:
- TUYỆT ĐỐI KHÔNG đọc, in, ghi log, commit hoặc trả về client nội dung file `.env`, connection string, JWT secret, mật khẩu DB, API keys.
- Cấu hình môi trường phải được inject qua `@Value` hoặc `@ConfigurationProperties` từ các biến môi trường được chuẩn hóa.
- Tuyệt đối không hardcode credentials trong source code, script, hoặc test files.

### V. Uniform Error Handling & API Response Envelope (Phản hồi API và xử lý lỗi đồng nhất)
Mọi API trả về phía client phải tuân theo cấu trúc chuẩn:
- Mọi endpoint Controller BẮT BUỘC bọc dữ liệu trong `ApiResponse<T>` (`ApiResponse.ok(...)`, `ApiResponse.created(...)`).
- Lỗi nghiệp vụ BẮT BUỘC được ném qua `BusinessException` kèm theo `ErrorDescriptor` (định nghĩa trong package `share.exception...`).
- Không trả về raw exception, không tự tạo exception handler cục bộ cạnh tranh với Global Exception Handler.

## Technical Stack & Architectural Constraints

- **Ngôn ngữ & Nền tảng**: Java 21, Spring Boot 3.5.0, Maven.
- **Kiến trúc Monolith**: Duy trì toàn bộ hệ thống trong monolithic service hiện tại; không tự ý chia tách microservices.
- **Cơ sở dữ liệu**: MongoDB là cơ sở dữ liệu chính; Redis dùng cho bộ nhớ đệm (caching) và lưu trữ tạm thời. Không bổ sung DB khác khi chưa được phê duyệt.
- **Quản lý Thư viện**: Tuân thủ phiên bản đã khai báo trong `pom.xml`. Không tự ý nâng cấp phiên bản Spring Boot, Java hoặc các thư viện nền tảng.

## Development & Verification Workflow

- **Khảo sát trước khi viết mã (Inspect First)**: Luôn kiểm tra các module và quy ước sẵn có trong codebase trước khi tạo mới lớp hoặc cấu trúc package.
- **Phạm vi tập trung (No Unrelated Refactoring)**: Chỉ sửa đổi mã nguồn liên quan trực tiếp đến tính năng yêu cầu; không refactor lan man mã nguồn không liên quan.
- **Xác thực thực tế (Real Verification)**: Mọi thay đổi mã nguồn phải được kiểm tra tính tương thích bằng lệnh biên dịch Maven thực tế (`./mvnw compile -DskipTests`). Không được báo thành công nếu chưa thực sự chạy lệnh kiểm chứng.

## Governance

- Bản Constitution này có hiệu lực cao nhất đối với toàn bộ hoạt động thiết kế, triển khai và bảo trì mã nguồn trong ZenFit backend.
- Mọi bổ sung, điều chỉnh quy tắc phải được ghi nhận phiên bản theo SemVer (Major: thay đổi nguyên tắc cốt lõi, Minor: mở rộng hướng dẫn/quy tắc mới, Patch: chỉnh sửa câu chữ, làm rõ).
- Các lệnh tạo spec/plan/tasks trong Spec Kit bắt buộc đối chiếu với Constitution này để đảm bảo tính tuân thủ trước khi triển khai.

**Version**: 1.0.0 | **Ratified**: 2026-11-01 | **Last Amended**: 2026-11-01
