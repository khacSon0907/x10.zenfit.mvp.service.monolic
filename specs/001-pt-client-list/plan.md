# Implementation Plan: PT Client List Pagination, Search, and Filtering

**Branch**: `001-pt-client-list` | **Date**: 2026-11-01 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-pt-client-list/spec.md`

## Summary

Triển khai tính năng phân trang, tìm kiếm, lọc và sắp xếp danh sách khách hàng dành cho Huấn luyện viên cá nhân (PT).
- **Phân trang**: Cố định 8 khách hàng mỗi trang (1-based index), kèm thông tin tổng số khách hàng (`totalElements`) và tổng số trang (`totalPages`).
- **Cô lập dữ liệu**: PT chỉ thấy và đếm các khách hàng được gán cho mình (`assignedTrainerId`), trích xuất an toàn từ `SecurityContext`.
- **Tìm kiếm & Lọc**: Tìm kiếm theo một phần họ tên hoặc số điện thoại (case-insensitive regex); lọc theo trạng thái (`ClientStatus`).
- **Sắp xếp**: Sắp xếp theo ngày tạo (`createdAt`) hoặc tên (`fullName`), mặc định là mới nhất trước (`createdAt DESC`).
- **Kết quả rỗng**: Trả về danh sách rỗng êm đẹp (HTTP 200 OK) khi không có bản ghi phù hợp.

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 3.5.0 (Spring Web, Spring Security, Spring Data MongoDB, Lombok, Jakarta Validation)

**Storage**: MongoDB (collection: `clients`)

**Testing**: JUnit 5, Spring Boot Test, Mockito

**Target Platform**: Linux / Windows JVM runtime (Monolithic Web Service)

**Project Type**: Monolithic Web REST API (`x10.zenfit.mvp.service`)

**Performance Goals**: < 1.0 giây thời gian phản hồi cho truy vấn phân trang và tìm kiếm với hàng chục nghìn bản ghi học viên

**Constraints**:
- Strict PT data isolation: Không rò rỉ khách hàng của PT khác
- Page size cố định là 8 khách hàng/trang
- 1-based page numbering trong request/response

**Scale/Scope**: Hỗ trợ toàn bộ PT và hàng nghìn học viên trong hệ thống ZenFit

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle / Rule | Compliance Status | Analysis & Verification |
| :--- | :--- | :--- |
| **I. Layering & Unidirectional Dependency** | **PASS** | Luồng xử lý: `ClientController -> ICoreClientService / IGetPtClientsUc -> GetPtClientsUcImpl -> IClientRepository -> ClientRepositoryImpl -> MongoTemplate / ClientDocument`. Controller không truy cập DB. |
| **II. Entity vs Document Separation** | **PASS** | `ClientEntity` nằm trong `common.domain.entities` không chứa annotation DB. `ClientDocument` nằm trong `infrastructure.client.db.mongodb`. Mapper chuyển đổi tại `ClientMapper`. |
| **III. Interface-Driven Use Case** | **PASS** | Use case đóng gói độc lập trong `service.clientService.getPtClientsUc` với interface `IGetPtClientsUc` và triển khai `GetPtClientsUcImpl` (`@Component`). |
| **IV. Zero Secret Exposure** | **PASS** | Không hardcode hoặc sử dụng secret. `trainerId` được lấy từ Authentication Principal. |
| **V. Uniform API Response Envelope** | **PASS** | API trả về bọc trong `ApiResponse<PaginatedClientResponse>` với `status = 200`, `code = "CLIENT.GET_PAGINATED_SUCCESS"`. |

## Project Structure

### Documentation (this feature)

```text
specs/001-pt-client-list/
├── plan.md              # Implementation Plan
├── research.md          # Phase 0: Technical decisions & research
├── data-model.md        # Phase 1: Data structures, DTOs & pagination logic
├── quickstart.md        # Phase 1: End-to-end curl validation guide
├── contracts/           # Phase 1: OpenAPI contract definition
│   └── pt-client-list-api.json
└── checklists/
    └── requirements.md  # Spec quality validation checklist
```

### Source Code (repository root)

```text
src/main/java/x10/zenfit/mvp/service/
├── controller/
│   └── ClientController.java                         # Thêm GET /api/clients/my-clients
├── service/
│   └── clientService/
│       ├── ICoreClientService.java                   # Thêm phương thức getPtClients
│       ├── CoreClientServiceImpl.java                # Delegate sang IGetPtClientsUc
│       └── getPtClientsUc/                           # Use case độc lập
│           ├── IGetPtClientsUc.java                  # Interface
│           ├── GetPtClientsUcImpl.java               # Triển khai use case logic
│           ├── GetPtClientsQuery.java                # DTO truy vấn đầu vào
│           └── PaginatedClientResponse.java          # DTO kết quả phân trang (items, page, totalPages,...)
├── Repository/
│   └── IClientRepository.java                        # Thêm phương thức findPtClientsWithPaging
└── infrastructure/
    └── client/
        └── adapter/
            └── ClientRepositoryImpl.java             # Triển khai truy vấn MongoTemplate với Criteria & Skip/Limit
```

**Structure Decision**: Tuân thủ 100% cấu trúc kiến trúc Monolithic và quy tắc phân tầng của ZenFit theo đúng quy định trong `AGENTS.md` và `constitution.md`.

## Complexity Tracking

> Không có vi phạm Constitution nào. Thiết kế tối giản, tái sử dụng các layer hiện có.

| Violation | Why Needed | Simpler Alternative Rejected Because |
| :--- | :--- | :--- |
| *None* | N/A | N/A |
