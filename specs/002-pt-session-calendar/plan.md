# Implementation Plan: Training Session Management & PT Calendar

**Branch**: `002-pt-session-calendar` | **Date**: 2026-11-03 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/002-pt-session-calendar/spec.md`

## Summary

Triển khai tính năng Quản lý buổi tập PT (Training Session Management) và Lịch làm việc PT (PT Calendar) cho ZenFit Backend:
- **Tạo & Cập nhật buổi tập**: Ràng buộc học viên (`Client`), huấn luyện viên (`Trainer`), hợp đồng PT (`PtContract` ở trạng thái `ACTIVE`), kiểm tra chống xung đột lịch theo cặp `(trainingDate, startTime)`.
- **PT Calendar**: Cung cấp API trực quan hỗ trợ xem theo Ngày (`DAY`), Tuần (`WEEK`: Thứ Hai đến Chủ Nhật) và Tháng (`MONTH`), tự động tính toán khoảng thời gian ở phía server, sắp xếp tăng dần theo ngày và giờ.
- **Vòng đời buổi tập**: Kiểm soát quy trình chuyển trạng thái nghiêm ngặt (`SCHEDULED` -> `CONFIRMED` -> `COMPLETED` / `CANCELLED` / `NO_SHOW`), tự động gán thời gian máy chủ cho `checkInAt` và `completedAt`.
- **Phân quyền & Bảo mật**: Phân lập nghiêm ngặt phạm vi dữ liệu của PT dựa trên `SecurityContext`, bảo đảm PT không thể can thiệp hoặc xem lịch của PT khác; Quản lý/Admin có quyền giám sát toàn diện.

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 3.5.0 (Spring Web, Spring Security, Spring Data MongoDB, Lombok, Jakarta Validation)

**Storage**: MongoDB (collection: `training_sessions`)

**Testing**: JUnit 5, Spring Boot Test, Mockito

**Target Platform**: Linux / Windows JVM runtime (Monolithic Web Service)

**Project Type**: Monolithic Web REST API (`x10.zenfit.mvp.service`)

**Performance Goals**: < 1.0 giây thời gian phản hồi cho truy vấn Calendar (khoảng 1 tháng dữ liệu) và danh sách phân trang; < 500ms cho các thao tác check-in/hoàn thành buổi tập.

**Constraints**:
- Tuân thủ nghiêm ngặt Domain Entity `TrainingSessionEntity` hiện tại: không thêm field `endTime`, `duration` hay `recurrence`.
- Kiểm tra xung đột lịch chính xác theo `(trainingDate, startTime)`.
- Không tự ý trừ số buổi trong hợp đồng PT (`PtContractEntity.usedSessions`) trong phiên bản v1.
- Phản hồi đồng nhất qua `ApiResponse<T>`, quản lý lỗi qua `BusinessException` và `TrainingSessionError`.

**Scale/Scope**: Hỗ trợ toàn bộ đội ngũ PT và học viên trên toàn hệ thống ZenFit.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle / Rule | Compliance Status | Analysis & Verification |
| :--- | :--- | :--- |
| **I. Layering & Unidirectional Dependency** | **PASS** | Luồng xử lý: `TrainingSessionController -> ICoreTrainingSessionService / Specific Use Cases -> ITrainingSessionRepository -> TrainingSessionRepositoryImpl -> MongoTemplate / TrainingSessionDocument`. Controller không truy cập DB. |
| **II. Entity vs Document Separation** | **PASS** | `TrainingSessionEntity` nằm trong `common.domain.entities.session` không chứa annotation DB. `TrainingSessionDocument` nằm trong `infrastructure.session.db.mongodb`. Ánh xạ độc lập qua `TrainingSessionMapper`. |
| **III. Interface-Driven Use Case** | **PASS** | Nghiệp vụ được chia tách thành các use case độc lập trong `service.trainingSessionService.*Uc` với interface và class triển khai `@Component`. Facade Service đóng vai trò điều phối. |
| **IV. Zero Secret Exposure** | **PASS** | Không hardcode hoặc sử dụng secret. `currentUserId` và vai trò được lấy an toàn từ `SecurityContext`. |
| **V. Uniform API Response Envelope** | **PASS** | Mọi API đều trả về qua `ApiResponse<T>` (`ApiResponse.ok(...)`, `ApiResponse.created(...)`). Mã lỗi tập trung trong `TrainingSessionError`. |

## Project Structure

### Documentation (this feature)

```text
specs/002-pt-session-calendar/
├── plan.md              # Implementation Plan
├── research.md          # Phase 0: Technical decisions & research
├── data-model.md        # Phase 1: Data structures, DTOs & state transitions
├── quickstart.md        # Phase 1: End-to-end curl validation guide
├── contracts/           # Phase 1: OpenAPI contract definition
│   └── training-session-calendar-api.json
└── checklists/
    └── requirements.md  # Spec quality validation checklist
```

### Source Code (repository root)

```text
src/main/java/x10/zenfit/mvp/service/
├── common/domain/
│   ├── entities/session/
│   │   └── TrainingSessionEntity.java                        # Domain Entity có sẵn
│   └── enums/
│       └── TrainingSessionStatus.java                        # Cập nhật enum: SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
├── controller/
│   └── TrainingSessionController.java                        # REST Controller cho /api/training-sessions
├── Repository/
│   └── ITrainingSessionRepository.java                       # Interface trừu tượng tầng dữ liệu
├── infrastructure/session/
│   ├── db/mongodb/
│   │   ├── TrainingSessionDocument.java                      # MongoDB Document
│   │   └── TrainingSessionMongoRepository.java               # Spring Data Mongo Repository
│   └── adapter/
│       ├── TrainingSessionRepositoryImpl.java                # Triển khai ITrainingSessionRepository
│       └── mapper/
│           └── TrainingSessionMapper.java                    # Chuyển đổi Document <-> Entity
├── service/trainingSessionService/
│   ├── ICoreTrainingSessionService.java                      # Facade Interface
│   ├── CoreTrainingSessionServiceImpl.java                   # Facade Implementation
│   ├── createSessionUc/                                      # Use case tạo buổi tập
│   │   ├── ICreateTrainingSessionUc.java
│   │   ├── CreateTrainingSessionUcImpl.java
│   │   └── CreateTrainingSessionRequest.java
│   ├── getSessionUc/                                         # Use case xem chi tiết
│   │   ├── IGetTrainingSessionUc.java
│   │   └── GetTrainingSessionUcImpl.java
│   ├── getSessionsUc/                                        # Use case danh sách phân trang & lọc
│   │   ├── IGetTrainingSessionsUc.java
│   │   ├── GetTrainingSessionsUcImpl.java
│   │   ├── TrainingSessionFilterQuery.java
│   │   └── PaginatedTrainingSessionResponse.java
│   ├── getCalendarUc/                                        # Use case Calendar
│   │   ├── IGetPtCalendarUc.java
│   │   ├── GetPtCalendarUcImpl.java
│   │   ├── PtCalendarQuery.java
│   │   ├── CalendarViewType.java                             # DAY, WEEK, MONTH
│   │   ├── TrainingSessionCalendarResponse.java
│   │   └── TrainingSessionCalendarItemResponse.java
│   ├── updateSessionUc/                                      # Use case cập nhật buổi tập
│   │   ├── IUpdateTrainingSessionUc.java
│   │   ├── UpdateTrainingSessionUcImpl.java
│   │   └── UpdateTrainingSessionRequest.java
│   ├── checkInSessionUc/                                     # Use case check-in
│   │   ├── ICheckInTrainingSessionUc.java
│   │   └── CheckInTrainingSessionUcImpl.java
│   ├── completeSessionUc/                                    # Use case hoàn thành buổi tập
│   │   ├── ICompleteTrainingSessionUc.java
│   │   └── CompleteTrainingSessionUcImpl.java
│   ├── cancelSessionUc/                                      # Use case hủy buổi tập
│   │   ├── ICancelTrainingSessionUc.java
│   │   └── CancelTrainingSessionUcImpl.java
│   └── noShowSessionUc/                                      # Use case đánh dấu vắng mặt
│       ├── INoShowTrainingSessionUc.java
│       └── NoShowTrainingSessionUcImpl.java
└── share/exception/session/
    └── TrainingSessionError.java                             # Mã lỗi nghiệp vụ buổi tập
```

**Structure Decision**: Tuân thủ 100% cấu trúc kiến trúc Monolithic và quy tắc phân tầng của ZenFit theo đúng quy định trong `AGENTS.md` và `constitution.md`.

## Complexity Tracking

> Không có vi phạm Constitution nào. Thiết kế tối giản, tái sử dụng các layer hiện có.

| Violation | Why Needed | Simpler Alternative Rejected Because |
| :--- | :--- | :--- |
| *None* | N/A | N/A |
