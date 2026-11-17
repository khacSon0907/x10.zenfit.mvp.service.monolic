# Feature Specification: Admin CRUD Subscription Plans for PT & Client Limits

**Feature Branch**: `003-subscription-plans`

**Created**: 2026-11-17

**Status**: Draft

**Input**: User description: "hiện tôi đã tạo enties bây giờ tôi cần sếp viết chô tôi flow admin tạo ra cái gói cho user là pt nè quản lý đồ sài , khi họ có quá nhiều học viên thì mình sẽ thu phí , gói free hiện tại thì mỗi pt được lưu bao nhiều học viên thì do admin chính và quy định bây giờ viết cho tôi 1 bộ api crud trước đi ạ"

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Admin tạo gói dịch vụ mới (Priority: P1)

Là một Quản trị viên (Admin), tôi muốn tạo các gói dịch vụ (Subscription Plan) với các hạn mức số lượng học viên (`maxStudents`), số buổi tập (`maxTrainingSessions`), giá tiền (`price`), và thời hạn (`durationDays`) để phân cấp tính năng cho Huấn luyện viên (PT) và Quản lý.

**Why this priority**: Đây là nền tảng cốt lõi của mô hình kinh doanh SaaS/thu phí của ZenFit. Không có API tạo gói, hệ thống không thể định nghĩa quy tắc thu phí hay hạn mức học viên.

**Independent Test**: Gọi API `POST /api/subscription-plans` với payload hợp lệ và nhận lại `ApiResponse.created(...)` với dữ liệu gói vừa tạo được lưu thành công trong MongoDB.

**Acceptance Scenarios**:
1. **Given** Admin đã đăng nhập với quyền `ROLE_ADMIN`, **When** gửi request tạo gói với mã `PRO_PT_MONTHLY`, tên `Gói PT Pro 1 Tháng`, giá `199000`, `durationDays = 30`, `maxStudents = 30`, `status = ACTIVE`, **Then** hệ thống lưu gói vào cơ sở dữ liệu và trả về mã HTTP 201 Created.
2. **Given** Admin gửi request tạo gói có mã `code` đã tồn tại trong hệ thống, **When** thực hiện tạo gói, **Then** hệ thống ném `BusinessException(SubscriptionPlanError.CODE_ALREADY_EXISTS)` và trả về mã HTTP 409 Conflict.
3. **Given** Admin tạo gói mới với `isDefault = true`, **When** tạo thành công, **Then** hệ thống tự động gỡ trạng thái `isDefault = false` ở gói mặc định cũ để đảm bảo toàn hệ thống chỉ có DUY NHẤT một gói mặc định.

---

### User Story 2 - Xem danh sách và chi tiết gói dịch vụ (Priority: P1)

Là một Quản trị viên hoặc Huấn luyện viên (PT/User), tôi muốn xem danh sách các gói dịch vụ và thông tin chi tiết từng gói để nắm được quyền lợi và lựa chọn đăng ký.

**Why this priority**: PT cần xem danh sách gói kích hoạt (`ACTIVE`) để lựa chọn nâng cấp khi đạt giới hạn học viên; Admin cần xem toàn bộ danh sách (kể cả `INACTIVE` và `ARCHIVED`) để quản lý.

**Independent Test**:
- PT/User gọi `GET /api/subscription-plans` nhận danh sách các gói `ACTIVE` sắp xếp theo `displayOrder`.
- Admin gọi `GET /api/subscription-plans/admin` nhận danh sách phân trang kèm bộ lọc `status` và `keyword`.
- Gọi `GET /api/subscription-plans/{id}` trả về thông tin chi tiết gói tương ứng.

**Acceptance Scenarios**:
1. **Given** các gói dịch vụ có trạng thái khác nhau (`ACTIVE`, `INACTIVE`, `ARCHIVED`), **When** PT gọi `GET /api/subscription-plans`, **Then** chỉ những gói có trạng thái `ACTIVE` mới được trả về, sắp xếp tăng dần theo `displayOrder`.
2. **Given** Admin muốn xem toàn bộ các gói trong hệ thống, **When** Admin gọi `GET /api/subscription-plans/admin?status=INACTIVE&page=0&size=10`, **Then** hệ thống trả về kết quả phân trang chính xác theo tiêu chí lọc.
3. **Given** ID gói dịch vụ không tồn tại, **When** gọi `GET /api/subscription-plans/{id}`, **Then** hệ thống trả về HTTP 404 Not Found kèm `SubscriptionPlanError.PLAN_NOT_FOUND`.

---

### User Story 3 - Admin cập nhật thông tin và trạng thái gói dịch vụ (Priority: P1)

Là một Quản trị viên, tôi muốn cập nhật thông tin (tên, mô tả, giá, thời hạn, giới hạn học viên, thứ tự hiển thị) hoặc chuyển đổi trạng thái (`ACTIVE`, `INACTIVE`, `ARCHIVED`) của gói dịch vụ.

**Why this priority**: Thị trường và chiến lược kinh doanh thường xuyên thay đổi (điều chỉnh giá, thay đổi số lượng học viên tối đa cho gói free, ẩn gói tạm thời hoặc lưu trữ gói cũ).

**Independent Test**: Gọi `PUT /api/subscription-plans/{id}` để cập nhật nội dung gói, hoặc `PATCH /api/subscription-plans/{id}/status` để đổi trạng thái, kiểm tra dữ liệu trả về và trong cơ sở dữ liệu đã thay đổi.

**Acceptance Scenarios**:
1. **Given** một gói dịch vụ đang tồn tại, **When** Admin gửi request cập nhật `maxStudents` từ 5 lên 10 cho gói Free mặc định, **Then** hệ thống cập nhật thành công và ghi nhận `updatedAt`.
2. **Given** Admin muốn ngừng kinh doanh một gói, **When** gửi request đổi trạng thái sang `INACTIVE` hoặc `ARCHIVED`, **Then** hệ thống cập nhật trạng thái gói và các PT không còn thấy gói này trong danh sách đăng ký public.

---

### User Story 4 - Quản trị gói mặc định miễn phí (Priority: P2)

Là một Quản trị viên, tôi muốn thiết lập một gói dịch vụ làm gói mặc định (`isDefault = true`) để hệ thống tự động gán cho PT khi mới tạo tài khoản, trong đó quy định rõ số lượng học viên tối đa được quản lý miễn phí (ví dụ: 5 học viên).

**Why this priority**: Giúp hiện thực hóa nghiệp vụ: "gói free hiện tại thì mỗi pt được lưu bao nhiêu học viên thì do admin chỉnh và quy định".

**Independent Test**: Thiết lập một gói làm mặc định và kiểm tra API truy vấn gói mặc định `GET /api/subscription-plans/default`.

**Acceptance Scenarios**:
1. **Given** hệ thống đang có gói A là `isDefault = true`, **When** Admin cập nhật gói B thành `isDefault = true`, **Then** gói A tự động chuyển thành `isDefault = false` và gói B trở thành gói mặc định duy nhất.
2. **Given** Admin cố gắng xóa hoặc lưu trữ (`ARCHIVED`) gói đang là `isDefault = true`, **When** thực hiện hành động, **Then** hệ thống từ chối với lỗi `CANNOT_DELETE_DEFAULT_PLAN`.

---

### User Story 5 - Xóa hoặc lưu trữ gói dịch vụ (Priority: P2)

Là một Quản trị viên, tôi muốn xóa gói dịch vụ không còn sử dụng để giữ bảng dữ liệu gọn gàng và tránh nhầm lẫn.

**Why this priority**: Quản lý vòng đời dữ liệu an toàn, chống việc vô tình xóa gói đang có người đăng ký.

**Independent Test**: Gọi `DELETE /api/subscription-plans/{id}` và kiểm tra gói bị xóa hoặc chuyển sang `ARCHIVED`.

**Acceptance Scenarios**:
1. **Given** gói dịch vụ chưa có PT nào đăng ký sử dụng và không phải là gói mặc định, **When** Admin gọi `DELETE /api/subscription-plans/{id}`, **Then** gói bị xóa hoàn toàn khỏi cơ sở dữ liệu.
2. **Given** gói dịch vụ là gói mặc định (`isDefault = true`), **When** Admin gọi `DELETE`, **Then** hệ thống ném `CANNOT_DELETE_DEFAULT_PLAN`.

---

## Edge Cases

- **Trùng lặp mã gói (`code`)**: Không phân biệt chữ hoa chữ thường (case-insensitive trim), mã gói phải duy nhất trên toàn hệ thống.
- **Giá trị giới hạn âm**: `price`, `durationDays`, `maxStudents`, `maxTrainingSessions` không được là số âm (trừ trường hợp quy ước `-1` đại diện cho không giới hạn nếu cần).
- **Đảm bảo tính đơn nhất của gói Default**: Khi có cập nhật đồng thời nhiều gói thành `isDefault = true`, giao dịch hoặc logic service phải đảm bảo chỉ duy nhất một gói giữ cờ này.
- **Phân quyền truy cập**: Các API ghi (Tạo, Sửa, Đổi trạng thái, Xóa) bắt buộc yêu cầu quyền `ROLE_ADMIN`. PT hoặc Staff thông thường cố tình gọi API admin sẽ nhận HTTP 403 Forbidden.

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Hệ thống BẮT BUỘC cung cấp API `POST /api/subscription-plans` chỉ cho phép `ROLE_ADMIN` tạo gói dịch vụ mới.
- **FR-002**: Hệ thống BẮT BUỘC kiểm tra tính duy nhất của `code` khi tạo mới hoặc cập nhật.
- **FR-003**: Hệ thống BẮT BUỘC đảm bảo tại mọi thời điểm chỉ có tối đa 1 gói dịch vụ có cờ `isDefault = true`.
- **FR-004**: Hệ thống BẮT BUỘC cung cấp API `GET /api/subscription-plans` cho phép PT/User xem danh sách các gói dịch vụ có trạng thái `ACTIVE`, sắp xếp theo `displayOrder` tăng dần.
- **FR-005**: Hệ thống BẮT BUỘC cung cấp API `GET /api/subscription-plans/admin` hỗ trợ phân trang (`page`, `size`) và lọc theo `status`, tìm kiếm theo `keyword` (mã hoặc tên).
- **FR-006**: Hệ thống BẮT BUỘC cung cấp API `GET /api/subscription-plans/{id}` để xem chi tiết gói dịch vụ.
- **FR-007**: Hệ thống BẮT BUỘC cung cấp API `PUT /api/subscription-plans/{id}` cho phép `ROLE_ADMIN` cập nhật thông tin gói.
- **FR-008**: Hệ thống BẮT BUỘC cung cấp API `PATCH /api/subscription-plans/{id}/status` để cập nhật trạng thái (`ACTIVE`, `INACTIVE`, `ARCHIVED`).
- **FR-009**: Hệ thống BẮT BUỘC cung cấp API `DELETE /api/subscription-plans/{id}` cho phép `ROLE_ADMIN` xóa gói, đồng thời ngăn chặn xóa gói mặc định (`isDefault = true`).
- **FR-010**: Mọi phản hồi API BẮT BUỘC được bọc trong cấu trúc `ApiResponse<T>` theo quy chuẩn của ZenFit Constitution.

### Key Entities

- **SubscriptionPlanEntity** (trong `x10.zenfit.mvp.service.common.domain.entities.subscription`):
  - `id`: Định danh duy nhất (MongoDB ObjectId dạng chuỗi)
  - `code`: Mã định danh gói (ví dụ: `FREE_PT`, `PRO_MONTHLY`)
  - `name`: Tên gói dịch vụ
  - `description`: Mô tả chi tiết quyền lợi gói
  - `price`: Giá gói dịch vụ (VND)
  - `durationDays`: Chu kỳ sử dụng (ngày)
  - `maxStudents`: Giới hạn số học viên PT được quản lý
  - `maxTrainingSessions`: Giới hạn số buổi tập (hoặc mở rộng)
  - `status`: Trạng thái gói (`ACTIVE`, `INACTIVE`, `ARCHIVED`)
  - `isDefault`: Cờ chỉ định gói miễn phí mặc định cho PT mới
  - `displayOrder`: Thứ tự hiển thị trên giao diện
  - `createdAt`, `updatedAt`: Dấu thời gian hệ thống

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% các API CRUD cho `SubscriptionPlan` được bảo vệ theo đúng phân quyền (Admin CRUD, PT/Public Read).
- **SC-002**: Thời gian phản hồi trung bình của API lấy danh sách gói active < 100ms.
- **SC-003**: 100% lỗi nghiệp vụ (trùng mã code, xóa gói default, không tìm thấy gói) được xử lý qua `BusinessException` với HTTP status và mã lỗi chuẩn xác.
- **SC-004**: Đảm bảo toàn vẹn dữ liệu: Không bao giờ có nhiều hơn 1 gói `isDefault = true` tồn tại trong MongoDB.

---

## Assumptions

- Admin là người trực tiếp định cấu hình số lượng học viên của gói Free thông qua trường `maxStudents` của gói có `isDefault = true`.
- Giai đoạn hiện tại (v1) tập trung hoàn thành bộ API CRUD quản lý gói dịch vụ trước. Luồng thanh toán mua gói và luồng kiểm tra vượt ngưỡng học viên (quota enforcement) khi PT thêm học viên mới sẽ được kết nối ở giai đoạn tiếp theo.
