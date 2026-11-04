# Technical Research & Decisions: Training Session Management & PT Calendar

**Feature**: `002-pt-session-calendar`  
**Date**: 2026-11-03  
**Status**: Completed  

---

## 1. Nghiên cứu & Quyết định Kỹ thuật (Technical Decisions)

### Quyết định 1: Kiểm tra xung đột lịch (Conflict Detection) trong điều kiện thiếu `endTime` / `duration`

- **Bối cảnh**: `TrainingSessionEntity` hiện tại chỉ có 2 trường thời gian liên quan đến việc đặt lịch:
  - `trainingDate`: `LocalDate`
  - `startTime`: `LocalTime`
  Không có trường `endTime`, `duration` hay `slot`.
- **Quyết định**: 
  - Thực hiện kiểm tra trùng lịch dựa trên cặp khóa chính xác `(trainingDate, startTime)` cho cả PT và Học viên (`Client`).
  - Khi tạo mới hoặc cập nhật một buổi tập:
    1. Kiểm tra PT: Không được tồn tại buổi tập nào khác có cùng `trainerId`, `trainingDate`, `startTime` mà có `status != CANCELLED` (loại trừ chính buổi tập đang cập nhật nếu là thao tác update).
    2. Kiểm tra Học viên: Không được tồn tại buổi tập nào khác có cùng `clientId`, `trainingDate`, `startTime` mà có `status != CANCELLED` (loại trừ chính buổi tập đang cập nhật).
- **Lý do**:
  - Tuân thủ nghiêm ngặt nguyên tắc: **Không tự ý thêm field vào Domain Entity** và **Không tự đặt thời lượng mặc định giả định để tính toán khoảng thời gian (overlap)**.
  - Phù hợp với mô hình vận hành các buổi tập theo khung giờ bắt đầu chuẩn (ví dụ các slot cố định 08:00, 09:00, 10:00).
- **Đánh chỉ mục MongoDB hỗ trợ**:
  - `createIndex({ trainerId: 1, trainingDate: 1, startTime: 1 })`
  - `createIndex({ clientId: 1, trainingDate: 1, startTime: 1 })`

---

### Quyết định 2: Thuật toán tính khoảng thời gian cho PT Calendar (DAY, WEEK, MONTH)

- **Bối cảnh**: API `GET /api/training-sessions/calendar` nhận tham số `view` (`DAY | WEEK | MONTH`) và `date` (`LocalDate`). Backend chịu trách nhiệm tự động xác định `startDate` và `endDate`.
- **Quyết định**:
  - Sử dụng API chuẩn `java.time.LocalDate` và `java.time.DayOfWeek` của Java 21:
    - **DAY**:
      - `startDate = date`
      - `endDate = date`
    - **WEEK**:
      - Quy ước tuần bắt đầu vào **Thứ Hai (MONDAY)** và kết thúc vào **Chủ Nhật (SUNDAY)**.
      - `startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))`
      - `endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))`
    - **MONTH**:
      - Lấy toàn bộ ngày trong tháng chứa `date`.
      - `startDate = date.with(TemporalAdjusters.firstDayOfMonth())`
      - `endDate = date.with(TemporalAdjusters.lastDayOfMonth())`
  - **Sắp xếp**:
    - Sử dụng Spring Data Sort: `Sort.by(Sort.Direction.ASC, "trainingDate").and(Sort.by(Sort.Direction.ASC, "startTime"))`.
- **Lý do**:
  - Chuẩn hóa hoàn toàn múi giờ và lịch làm việc của phòng tập, không để Frontend phải tính toán sai lệch tuần hoặc tháng.

---

### Quyết định 3: Vòng đời và Ma trận chuyển đổi trạng thái (`TrainingSessionStatus`)

- **Bối cảnh**: Trạng thái buổi tập phản ánh tiến trình thực tế. Cần ngăn chặn việc nhảy trạng thái tùy tiện hoặc sửa đổi buổi tập đã kết thúc.
- **Tập hợp trạng thái**:
  - `SCHEDULED`: Buổi tập đã lên lịch, chưa diễn ra.
  - `CONFIRMED`: Buổi tập đã được xác nhận (hoặc đã check-in).
  - `COMPLETED`: Buổi tập đã hoàn thành tập luyện xong.
  - `CANCELLED`: Buổi tập đã bị hủy.
  - `NO_SHOW`: Học viên vắng mặt không lý do.
- **Ma trận chuyển trạng thái hợp lệ**:

| Trạng thái hiện tại | Thao tác cho phép | Trạng thái đích | Ghi chú |
| :--- | :--- | :--- | :--- |
| `SCHEDULED` | `check-in` | `CONFIRMED` | Ghi nhận `checkInAt = LocalDateTime.now()` |
| `SCHEDULED` | `complete` | `COMPLETED` | Ghi nhận `completedAt = LocalDateTime.now()` |
| `SCHEDULED` | `cancel` | `CANCELLED` | Ghi nhận `updatedAt` |
| `SCHEDULED` | `no-show` | `NO_SHOW` | Ghi nhận `updatedAt` |
| `CONFIRMED` | `complete` | `COMPLETED` | Ghi nhận `completedAt = LocalDateTime.now()` |
| `CONFIRMED` | `cancel` | `CANCELLED` | Hủy sau khi đã xác nhận |
| `CONFIRMED` | `no-show` | `NO_SHOW` | Đánh dấu vắng mặt |
| `COMPLETED` | *Không cho phép* | - | Trạng thái kết thúc (Terminal state) |
| `CANCELLED` | *Không cho phép* | - | Trạng thái kết thúc (Terminal state) |
| `NO_SHOW` | *Không cho phép* | - | Trạng thái kết thúc (Terminal state) |

- **Quy tắc an toàn thời gian**:
  - `checkInAt` và `completedAt` BẮT BUỘC được gán bởi đồng hồ máy chủ (`LocalDateTime.now()`), cấm tiếp nhận giá trị từ client payload.

---

### Quyết định 4: Cơ chế Phân quyền và Cô lập dữ liệu

- **Bối cảnh**: Hệ thống phục vụ nội bộ phòng tập gồm Ban quản lý (`ADMIN`, `HEAD_OF_TRAINING`, `TEAM_MANAGER`, `STAFF`) và Huấn luyện viên (`PT`).
- **Quyết định**:
  - Trích xuất danh tính và vai trò từ `SecurityContextHolder.getContext().getAuthentication()`.
  - **Nhóm Quản lý (Management)**:
    - Được phép xem, tạo, cập nhật, hủy lịch của bất kỳ PT nào.
    - Được phép lọc theo `trainerId`, `clientId`, `contractId`, `status`.
  - **Huấn luyện viên (PT)**:
    - Khi xem Calendar hoặc danh sách: Hệ thống bắt buộc lọc theo `trainerId = currentUserId`.
    - Khi tạo buổi tập: Nếu không truyền `trainerId`, tự động gán `trainerId = currentUserId`. Nếu truyền `trainerId` khác, từ chối quyền (403 Forbidden).
    - Khi cập nhật/hủy/check-in/complete: Kiểm tra buổi tập đó có `trainerId == currentUserId` hay không. Nếu không, từ chối (403 Forbidden).

---

### Quyết định 5: Liên kết Hợp đồng PT (`PtContractEntity`)

- **Bối cảnh**: Mỗi buổi tập bắt buộc gắn với một hợp đồng dịch vụ `contractId`.
- **Quyết định**:
  - Khi tạo buổi tập, kiểm tra qua `IPtContractRepository`:
    1. Hợp đồng tồn tại (`PtContractError.CONTRACT_NOT_FOUND`).
    2. Hợp đồng thuộc đúng học viên: `contract.getClientId().equals(clientId)`.
    3. Hợp đồng đang hoạt động: `contract.getStatus() == ContractStatus.ACTIVE`.
  - **Xử lý số buổi hợp đồng (`usedSessions`)**:
    - Tuân thủ nguyên tắc yêu cầu: Không tự ý gọi cập nhật tăng `usedSessions` trong `PtContractEntity` trong phiên bản v1, nhằm tránh can thiệp ngoài phạm vi thiết kế hợp đồng hiện tại.

---

## 2. Kiến trúc & Cấu trúc Package

Tuân thủ nghiêm ngặt Constitution v1.0.0 của ZenFit:
- **Domain Layer**:
  - Entity: `common.domain.entities.session.TrainingSessionEntity`
  - Enum: `common.domain.enums.TrainingSessionStatus`
- **Application Layer (Use Cases)**:
  - Package: `service.trainingSessionService`
  - Mỗi use case nằm trong thư mục riêng: `createSessionUc`, `getSessionUc`, `getSessionsUc`, `getCalendarUc`, `updateSessionUc`, `checkInSessionUc`, `completeSessionUc`, `cancelSessionUc`, `noShowSessionUc`.
  - Facade Service: `ICoreTrainingSessionService` & `CoreTrainingSessionServiceImpl`.
- **Infrastructure Layer**:
  - Document: `infrastructure.session.db.mongodb.TrainingSessionDocument`
  - Mongo Repository: `infrastructure.session.db.mongodb.TrainingSessionMongoRepository`
  - Repository Impl: `infrastructure.session.adapter.TrainingSessionRepositoryImpl`
  - Mapper: `infrastructure.session.adapter.mapper.TrainingSessionMapper`
- **Repository Interface**:
  - `Repository.ITrainingSessionRepository`
- **HTTP Controller**:
  - `controller.TrainingSessionController`
- **Error Handling**:
  - `share.exception.session.TrainingSessionError`

