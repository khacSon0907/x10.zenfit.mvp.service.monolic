---
description: "Task list for Training Session Management and PT Calendar feature implementation"
---

# Tasks: Training Session Management & PT Calendar

**Input**: Design documents from `specs/002-pt-session-calendar/`  
**Prerequisites**: [plan.md](plan.md), [spec.md](spec.md), [research.md](research.md), [data-model.md](data-model.md), [contracts/training-session-calendar-api.json](contracts/training-session-calendar-api.json), [quickstart.md](quickstart.md)  
**Organization**: Tasks are grouped by foundational architecture and user story phases to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4, US5)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Domain, Errors & Persistence Foundation)

**Purpose**: Thiết lập enum trạng thái, mã lỗi nghiệp vụ, Document MongoDB và Mapper nền tảng

- [x] T001 Update enum `TrainingSessionStatus.java` in `src/main/java/x10/zenfit/mvp/service/common/domain/enums/TrainingSessionStatus.java` with values: `SCHEDULED`, `CONFIRMED`, `COMPLETED`, `CANCELLED`, `NO_SHOW`
- [x] T002 [P] Create error descriptor enum `TrainingSessionError.java` in `src/main/java/x10/zenfit/mvp/service/share/exception/session/TrainingSessionError.java` implementing `ErrorDescriptor` with codes: `TRAINING_SESSION_NOT_FOUND` (404), `TRAINING_SESSION_CONFLICT` (409), `TRAINING_SESSION_INVALID_STATUS` (400), `TRAINING_SESSION_ACCESS_DENIED` (403), `TRAINING_SESSION_INVALID_DATA` (400), `CONTRACT_NOT_ACTIVE` (400), `CONTRACT_NOT_BELONG_TO_CLIENT` (400)
- [x] T003 [P] Create MongoDB document `TrainingSessionDocument.java` in `src/main/java/x10/zenfit/mvp/service/infrastructure/session/db/mongodb/TrainingSessionDocument.java` with collection `training_sessions`, fields: `id`, `clientId`, `trainerId`, `contractId`, `sessionName`, `trainingDate`, `startTime`, `status`, `checkInAt`, `completedAt`, `note`, `createdAt`, `updatedAt` and indexes: compound `{ "trainerId": 1, "trainingDate": 1, "startTime": 1 }`, compound `{ "clientId": 1, "trainingDate": 1, "startTime": 1 }`, compound `{ "trainerId": 1, "trainingDate": 1 }`, and single `{ "contractId": 1 }`
- [x] T004 [P] Create Spring Data Mongo repository `TrainingSessionMongoRepository.java` in `src/main/java/x10/zenfit/mvp/service/infrastructure/session/db/mongodb/TrainingSessionMongoRepository.java` extending `MongoRepository<TrainingSessionDocument, String>`
- [x] T005 [P] Create entity-document mapper `TrainingSessionMapper.java` in `src/main/java/x10/zenfit/mvp/service/infrastructure/session/adapter/mapper/TrainingSessionMapper.java` providing bidirectional mapping `toEntity(TrainingSessionDocument)` and `toDocument(TrainingSessionEntity)`

---

## Phase 2: Foundational (Repository & Security Architecture)

**Purpose**: Hoàn thiện tầng trừu tượng dữ liệu và tích hợp bảo mật Spring Security

- [x] T006 Define repository interface `ITrainingSessionRepository.java` in `src/main/java/x10/zenfit/mvp/service/Repository/ITrainingSessionRepository.java` with methods: `save(TrainingSessionEntity session)`, `findById(String id)`, `existsTrainerConflict(String trainerId, LocalDate trainingDate, LocalTime startTime, String excludeSessionId)`, `existsClientConflict(String clientId, LocalDate trainingDate, LocalTime startTime, String excludeSessionId)`
- [x] T007 Implement repository adapter `TrainingSessionRepositoryImpl.java` in `src/main/java/x10/zenfit/mvp/service/infrastructure/session/adapter/TrainingSessionRepositoryImpl.java` implementing `ITrainingSessionRepository` using `TrainingSessionMongoRepository` and `MongoTemplate` for conflict queries
- [x] T008 [P] Define facade service interface `ICoreTrainingSessionService.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/ICoreTrainingSessionService.java`
- [x] T009 Create facade implementation `CoreTrainingSessionServiceImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/CoreTrainingSessionServiceImpl.java` (`@Service`)
- [x] T010 Create REST controller `TrainingSessionController.java` in `src/main/java/x10/zenfit/mvp/service/controller/TrainingSessionController.java` with `@RequestMapping("/api/training-sessions")` and security context helper methods `getCurrentUserId()` and `isManagementUser()`
- [x] T011 Update route authorization in `src/main/java/x10/zenfit/mvp/service/config/security/SecurityConfig.java` to grant access to `/api/training-sessions` and `/api/training-sessions/**` for roles `"ADMIN", "HEAD_OF_TRAINING", "TEAM_MANAGER", "PT", "STAFF"`

**Checkpoint**: Nền tảng dữ liệu và bảo mật sẵn sàng - bắt đầu triển khai các User Story độc lập.

---

## Phase 3: User Story 1 - PT tạo lịch tập gắn với hợp đồng & Chống xung đột (Priority: P1) 🎯 MVP

**Goal**: Cho phép PT hoặc ADMIN tạo buổi tập mới gắn với học viên và hợp đồng có hiệu lực, kiểm tra xung đột trùng lịch chính xác theo `(trainingDate, startTime)`.

**Independent Test**: Gọi `POST /api/training-sessions` với `clientId`, `trainerId`, `contractId`, `sessionName`, `trainingDate`, `startTime`. Nhận về buổi tập ở trạng thái `SCHEDULED`, `checkInAt = null`, `completedAt = null`. Tạo lại buổi tập thứ hai cùng ngày và giờ thì bị chặn với mã lỗi xung đột 409.

- [x] T012 [P] [US1] Create request DTO `CreateTrainingSessionRequest.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/createSessionUc/CreateTrainingSessionRequest.java` with constraints: `clientId` (`@NotBlank`), `contractId` (`@NotBlank`), `sessionName` (`@NotBlank`, `@Size(max = 100)`), `trainingDate` (`@NotNull`), `startTime` (`@NotNull`), `trainerId` (optional String), `note` (`@Size(max = 1000)`)
- [x] T013 [P] [US1] Create response DTO `TrainingSessionResponse.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/TrainingSessionResponse.java` mapping full entity fields
- [x] T014 [P] [US1] Define use case interface `ICreateTrainingSessionUc.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/createSessionUc/ICreateTrainingSessionUc.java`
- [x] T015 [US1] Implement use case `CreateTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/createSessionUc/CreateTrainingSessionUcImpl.java` (`@Component`):
  1. Kiểm tra học viên tồn tại qua `IClientRepository`.
  2. Xác định `trainerId` (nếu PT gọi thì gán từ SecurityContext; nếu ADMIN gọi thì lấy từ request hoặc mặc định PT phân công của học viên).
  3. Kiểm tra hợp đồng tồn tại qua `IPtContractRepository`, kiểm tra `contract.getClientId().equals(clientId)` và `contract.getStatus() == ContractStatus.ACTIVE`.
  4. Kiểm tra xung đột lịch PT qua `ptContractRepository.existsTrainerConflict(trainerId, trainingDate, startTime, null)`.
  5. Kiểm tra xung đột lịch học viên qua `ptContractRepository.existsClientConflict(clientId, trainingDate, startTime, null)`.
  6. Khởi tạo `TrainingSessionEntity` với trạng thái `SCHEDULED`, `checkInAt = null`, `completedAt = null`, `createdAt = now()`, `updatedAt = now()`.
  7. Lưu qua `ITrainingSessionRepository.save` và trả về `TrainingSessionResponse`.
- [x] T016 [US1] Expose `POST /api/training-sessions` endpoint in `src/main/java/x10/zenfit/mvp/service/controller/TrainingSessionController.java` delegating to `coreTrainingSessionService.createTrainingSession(request)` and returning `ApiResponse.created("SESSION.CREATE_SUCCESS", "Tạo buổi tập thành công", response)`

**Checkpoint**: User Story 1 hoàn thành độc lập. Đạt mốc MVP cho việc khởi tạo buổi tập với kiểm tra ràng buộc toàn vẹn.

---

## Phase 4: User Story 2 - PT Calendar: Xem lịch làm việc theo Ngày, Tuần, Tháng (Priority: P1)

**Goal**: Cho phép PT hoặc ADMIN xem lịch làm việc trực quan theo Ngày (`DAY`), Tuần (`WEEK`: Thứ Hai đến Chủ Nhật) và Tháng (`MONTH`), backend tự động tính `startDate` và `endDate`, sắp xếp tăng dần theo `trainingDate` rồi `startTime`.

**Independent Test**: Gọi `GET /api/training-sessions/calendar?view=WEEK&date=2026-11-10`. Hệ thống tự động xác định `startDate=2026-11-09` (Thứ Hai) và `endDate=2026-11-15` (Chủ Nhật), trả về các buổi tập trong tuần được sắp xếp đúng thứ tự thời gian.

- [x] T017 [P] [US2] Create Calendar DTOs: `CalendarViewType.java` (`DAY`, `WEEK`, `MONTH`), `TrainingSessionCalendarItemResponse.java` (gồm `id`, `clientId`, `trainerId`, `contractId`, `sessionName`, `trainingDate`, `startTime`, `status`, `note`), và `TrainingSessionCalendarResponse.java` (gồm `view`, `startDate`, `endDate`, `totalSessions`, `sessions`) in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/getCalendarUc/`
- [x] T018 [US2] Add calendar query method `findSessionsForCalendar(String trainerId, String clientId, String contractId, TrainingSessionStatus status, LocalDate startDate, LocalDate endDate)` to `ITrainingSessionRepository.java` and implement in `src/main/java/x10/zenfit/mvp/service/infrastructure/session/adapter/TrainingSessionRepositoryImpl.java` using `MongoTemplate` sorted by `trainingDate ASC` and `startTime ASC`
- [x] T019 [P] [US2] Define use case interface `IGetPtCalendarUc.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/getCalendarUc/IGetPtCalendarUc.java`
- [x] T020 [US2] Implement `GetPtCalendarUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/getCalendarUc/GetPtCalendarUcImpl.java` (`@Component`):
  1. Xử lý khoảng ngày:
     - `DAY`: `startDate = date`, `endDate = date`.
     - `WEEK`: `startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))`, `endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))`.
     - `MONTH`: `startDate = date.with(TemporalAdjusters.firstDayOfMonth())`, `endDate = date.with(TemporalAdjusters.lastDayOfMonth())`.
  2. Áp dụng cô lập dữ liệu: nếu PT gọi, bắt buộc gán `trainerId = currentUserId`.
  3. Truy vấn danh sách qua `ITrainingSessionRepository.findSessionsForCalendar`.
  4. Đóng gói phản hồi `TrainingSessionCalendarResponse`.
- [x] T021 [US2] Expose `GET /api/training-sessions/calendar` endpoint in `src/main/java/x10/zenfit/mvp/service/controller/TrainingSessionController.java` receiving query params (`view`, `date`, `trainerId`, `clientId`, `contractId`, `status`), delegating to facade service, and returning `ApiResponse.ok("SESSION.CALENDAR_SUCCESS", "Lấy lịch làm việc PT thành công", response)`

**Checkpoint**: User Story 2 hoàn thành độc lập. PT và ADMIN có thể xem lịch trình linh hoạt theo ngày/tuần/tháng.

---

## Phase 5: User Story 3 - Vận hành vòng đời: Check-in, Hoàn thành, Vắng mặt, Hủy (Priority: P1)

**Goal**: Thực hiện điểm danh (Check-in), hoàn thành buổi tập (Complete), ghi nhận học viên vắng mặt (No-show), và hủy buổi tập (Cancel), tuân thủ nghiêm ngặt ma trận chuyển đổi trạng thái và gán thời gian máy chủ.

**Independent Test**: Gọi tuần tự `PATCH /{id}/check-in` (kiểm tra `checkInAt` được cập nhật, trạng thái `CONFIRMED`), gọi tiếp `PATCH /{id}/complete` (kiểm tra `completedAt` được cập nhật, trạng thái `COMPLETED`). Cố tình gọi hủy buổi tập đã hoàn thành sẽ bị từ chối với lỗi 400.

- [x] T022 [P] [US3] Create use case interface `ICheckInTrainingSessionUc.java` and implementation `CheckInTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/checkInSessionUc/`:
  - Kiểm tra buổi tập tồn tại.
  - Kiểm tra quyền thao tác (chỉ PT phụ trách hoặc ADMIN).
  - Kiểm tra trạng thái: chỉ cho phép check-in khi ở `SCHEDULED` hoặc `CONFIRMED`. Không cho phép nếu `COMPLETED`, `CANCELLED`, `NO_SHOW`.
  - Cập nhật `checkInAt = LocalDateTime.now()`, trạng thái sang `CONFIRMED` nếu đang `SCHEDULED`, `updatedAt = LocalDateTime.now()`.
- [x] T023 [P] [US3] Create use case interface `ICompleteTrainingSessionUc.java` and implementation `CompleteTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/completeSessionUc/`:
  - Kiểm tra buổi tập tồn tại và quyền thao tác.
  - Kiểm tra trạng thái: chỉ cho phép khi ở `SCHEDULED` hoặc `CONFIRMED`.
  - Cập nhật `completedAt = LocalDateTime.now()`, trạng thái sang `COMPLETED`, `updatedAt = LocalDateTime.now()`.
- [x] T024 [P] [US3] Create use case interface `INoShowTrainingSessionUc.java` and implementation `NoShowTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/noShowSessionUc/`:
  - Kiểm tra buổi tập tồn tại và quyền thao tác.
  - Kiểm tra trạng thái: không áp dụng với buổi tập đã `COMPLETED` hoặc `CANCELLED`.
  - Cập nhật trạng thái sang `NO_SHOW`, `updatedAt = LocalDateTime.now()`.
- [x] T025 [P] [US3] Create use case interface `ICancelTrainingSessionUc.java` and implementation `CancelTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/cancelSessionUc/`:
  - Kiểm tra buổi tập tồn tại và quyền thao tác.
  - Kiểm tra trạng thái: không cho phép hủy buổi tập đã `COMPLETED` hoặc đã `CANCELLED`.
  - Cập nhật trạng thái sang `CANCELLED`, `updatedAt = LocalDateTime.now()`, bảo lưu bản ghi không xóa cứng.
- [x] T026 [US3] Expose lifecycle endpoints in `src/main/java/x10/zenfit/mvp/service/controller/TrainingSessionController.java`:
  - `PATCH /api/training-sessions/{id}/check-in`
  - `PATCH /api/training-sessions/{id}/complete`
  - `PATCH /api/training-sessions/{id}/no-show`
  - `PATCH /api/training-sessions/{id}/cancel`

**Checkpoint**: User Story 3 hoàn thành độc lập. Toàn bộ vòng đời vận hành buổi tập được bảo vệ chặt chẽ.

---

## Phase 6: User Story 4 - Danh sách buổi tập phân trang, lọc & Xem chi tiết (Priority: P2)

**Goal**: Cho phép tra cứu danh sách các buổi tập dạng bảng với phân trang, lọc đa tiêu chí (theo học viên, PT, hợp đồng, trạng thái, khoảng ngày) và xem chi tiết một buổi tập theo ID.

**Independent Test**: Gọi `GET /api/training-sessions?page=1&size=10&status=COMPLETED`. Nhận về danh sách các buổi tập `COMPLETED` kèm metadata phân trang (`totalElements`, `totalPages`, `page`, `size`). Gọi `GET /api/training-sessions/{id}` nhận chi tiết buổi tập tương ứng.

- [x] T027 [P] [US4] Create query DTO `TrainingSessionFilterQuery.java` and response DTO `PaginatedTrainingSessionResponse.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/getSessionsUc/`
- [x] T028 [US4] Add pagination query method `findSessionsWithPaging(TrainingSessionFilterQuery query)` to `ITrainingSessionRepository.java` and implement in `src/main/java/x10/zenfit/mvp/service/infrastructure/session/adapter/TrainingSessionRepositoryImpl.java` with dynamic criteria, `count()`, and paginated `find()`
- [x] T029 [US4] Create use case interface `IGetTrainingSessionsUc.java` and implementation `GetTrainingSessionsUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/getSessionsUc/` (áp dụng cô lập PT, chuẩn hóa `page < 1` về `1`, trả về danh sách rỗng 200 OK nếu không có kết quả)
- [x] T030 [P] [US4] Create use case interface `IGetTrainingSessionUc.java` and implementation `GetTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/getSessionUc/` (kiểm tra quyền truy cập của PT trước khi trả về chi tiết)
- [x] T031 [US4] Expose endpoints in `src/main/java/x10/zenfit/mvp/service/controller/TrainingSessionController.java`:
  - `GET /api/training-sessions` (phân trang và lọc)
  - `GET /api/training-sessions/{id}` (chi tiết)

**Checkpoint**: User Story 4 hoàn thành độc lập. Cung cấp năng lực quản lý và tra cứu dữ liệu lịch sử hoàn chỉnh.

---

## Phase 7: User Story 5 - Cập nhật thông tin buổi tập & Kiểm tra lại xung đột (Priority: P2)

**Goal**: Cho phép cập nhật thông tin nghiệp vụ buổi tập (`sessionName`, `trainingDate`, `startTime`, `note`), ngăn sửa buổi tập đã kết thúc và kiểm tra lại xung đột lịch nếu thay đổi ngày hoặc giờ tập.

**Independent Test**: Gọi `PATCH /api/training-sessions/{id}` đổi giờ tập sang thời điểm mới. Hệ thống kiểm tra xung đột, lưu cập nhật và ghi nhận `updatedAt`. Nếu đổi sang giờ đã bận, hệ thống từ chối với lỗi 409.

- [x] T032 [P] [US5] Create request DTO `UpdateTrainingSessionRequest.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/updateSessionUc/UpdateTrainingSessionRequest.java` with fields: `sessionName`, `trainingDate`, `startTime`, `note`, `clientId`, `trainerId`, `contractId`
- [x] T033 [P] [US5] Define use case interface `IUpdateTrainingSessionUc.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/updateSessionUc/IUpdateTrainingSessionUc.java`
- [x] T034 [US5] Implement `UpdateTrainingSessionUcImpl.java` in `src/main/java/x10/zenfit/mvp/service/service/trainingSessionService/updateSessionUc/UpdateTrainingSessionUcImpl.java` (`@Component`):
  1. Kiểm tra buổi tập tồn tại và quyền thao tác (PT phụ trách hoặc ADMIN).
  2. Kiểm tra trạng thái: cấm cập nhật nếu buổi tập đã ở `COMPLETED` hoặc `CANCELLED`.
  3. Nếu thay đổi `trainingDate` hoặc `startTime`, kiểm tra lại xung đột cho PT và học viên (loại trừ `id` của chính buổi tập đang sửa).
  4. Cập nhật các trường được truyền, cập nhật `updatedAt = LocalDateTime.now()`.
  5. Lưu và trả về `TrainingSessionResponse`.
- [x] T035 [US5] Expose endpoint `PATCH /api/training-sessions/{id}` in `src/main/java/x10/zenfit/mvp/service/controller/TrainingSessionController.java` delegating to facade service

**Checkpoint**: User Story 5 hoàn thành độc lập. Lịch tập có thể được điều chỉnh linh hoạt và an toàn.

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Kiểm thử tích hợp toàn diện, biên dịch hệ thống và xác thực các kịch bản thực tế

- [x] T036 Execute Maven compilation `./mvnw compile "-DskipTests"` to verify zero compile errors across all 570+ files in the repository
- [x] T037 Validate all 7 end-to-end integration scenarios documented in `specs/002-pt-session-calendar/quickstart.md` (Tạo mới, Chống trùng lịch, Calendar Tuần, Check-in, Hoàn thành, Chặn thao tác sai trạng thái, Cô lập dữ liệu PT)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Không phụ thuộc - thực thi ngay lập tức.
- **Foundational (Phase 2)**: Phụ thuộc vào Phase 1 hoàn tất - **CHẶN** tất cả các User Stories tiếp theo.
- **User Story 1 (Phase 3 - P1 MVP)**: Phụ thuộc vào Phase 2 hoàn tất.
- **User Story 2 (Phase 4 - P1 Calendar)**: Phụ thuộc vào Phase 2 hoàn tất.
- **User Story 3 (Phase 5 - P1 Lifecycle)**: Phụ thuộc vào Phase 2 và dữ liệu buổi tập từ US1.
- **User Story 4 (Phase 6 - P2 List & Detail)**: Phụ thuộc vào Phase 2.
- **User Story 5 (Phase 7 - P2 Update)**: Phụ thuộc vào Phase 2 và logic xung đột từ US1.
- **Polish (Phase 8)**: Phụ thuộc vào việc hoàn tất các User Stories mong muốn.

### Parallel Opportunities

- Các task `[P]` trong Phase 1 (T002, T003, T004, T005) có thể triển khai song song vì tác động trên các file khác nhau.
- Các use case của User Story 3 (T022, T023, T024, T025) có thể triển khai song song trong các package riêng biệt.
- Sau khi Phase 2 hoàn thành, User Story 1 (Create) và User Story 2 (Calendar) có thể được tiến hành độc lập.

---

## Implementation Strategy

### MVP First (User Story 1 Only)
1. Hoàn thành Phase 1 (Setup) và Phase 2 (Foundational).
2. Hoàn thành Phase 3 (User Story 1 - Tạo buổi tập & Chống trùng lịch).
3. **DỪNG LẠI & KIỂM TRA**: Đảm bảo tạo buổi tập và chặn trùng lịch hoạt động 100%.

### Incremental Delivery
1. Giao đoạn 1: MVP (Tạo buổi tập + Ràng buộc hợp đồng).
2. Giai đoạn 2: PT Calendar (Xem lịch Ngày, Tuần, Tháng trực quan).
3. Giai đoạn 3: Vòng đời buổi tập (Check-in, Hoàn thành, Vắng mặt, Hủy).
4. Giai đoạn 4: Danh sách phân trang, Tra cứu chi tiết và Cập nhật lịch.
