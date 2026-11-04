# Research & Technical Decisions: PT Client List Pagination, Search, and Filtering

**Feature**: `001-pt-client-list`  
**Date**: 2026-11-01  
**Status**: Completed

---

## 1. Technical Context & Unknowns

The feature requires PTs to view their assigned clients in a paginated grid/list format (8 items per page), search by name or phone, filter by status, and sort by creation date or name.

Key technical questions investigated:
1. How should MongoDB querying, multi-field search (name or phone), status filtering, and sorting be structured within the Spring Boot architecture?
2. How should pagination metadata and 1-based vs 0-based page index mapping be handled?
3. How to guarantee strict PT data isolation (preventing PT from viewing other PTs' clients)?
4. How to format the response to adhere to Constitution Principle V (`ApiResponse<T>`)?

---

## 2. Research Findings & Decisions

### Decision 1: Query Execution Pattern (Spring Data Mongo `MongoTemplate` vs `Repository Query Methods`)
- **Decision**: Use `MongoTemplate` with `Criteria` and `Query` inside `ClientRepositoryImpl` (infrastructure layer), exposing clean pagination methods through `IClientRepository`.
- **Rationale**:
  - The query combines dynamic criteria: strict mandatory filter `assignedTrainerId`, optional search regex across multiple fields (`fullName` OR `phone`), optional `status` filter, dynamic sorting (`createdAt` or `fullName`), and pagination (`skip` + `limit`).
  - Spring Data Mongo Derived Query Methods become excessively rigid with optional dynamic filters. `MongoTemplate` allows building dynamic criteria cleanly and performs both a `count()` query (for total elements/pages) and a `find()` query with `skip((page - 1) * size)` and `limit(size)`.
- **Alternatives Considered**:
  - *Spring Data Mongo `@Query` with SpEL*: Harder to handle optional null parameters cleanly without messy regex evaluations.
  - *QueryDSL for MongoDB*: Requires additional code generation plugins and dependencies not present in `pom.xml` (violates Constitution rule on dependency management).

### Decision 2: Trainer Identity Extraction & Data Isolation
- **Decision**: Extract `trainerId` directly from the authenticated user's `SecurityContext` (`getCurrentUserId()`) in the Controller layer, passing it down to the Use Case.
- **Rationale**:
  - User Story 2 requires PTs to never access other PTs' clients. Trusting a `trainerId` passed as an arbitrary query parameter from the client allows authorization bypass (IDOR vulnerability).
  - Extracting `trainerId` from JWT Authentication guarantees that a PT can only query records where `assignedTrainerId == currentAuthenticatedUserId`.
- **Alternatives Considered**:
  - *Passing `trainerId` in request query parameter*: Insecure unless accompanied by role checks; could expose other trainers' clients.

### Decision 3: Pagination Standards & 1-Based Indexing
- **Decision**:
  - API accepts `page` starting at 1 (`page >= 1`). If `page < 1` or null, default to `1`.
  - Fixed default page size `size = 8`.
  - Return a structured DTO `PaginatedClientResponse`:
    - `items`: `List<ClientResponse>`
    - `page`: `int` (1-based)
    - `size`: `int` (8)
    - `totalElements`: `long`
    - `totalPages`: `int` (calculated as `ceil(totalElements / size)`)
    - `hasNext`: `boolean` (`page < totalPages`)
    - `hasPrevious`: `boolean` (`page > 1`)
- **Rationale**:
  - Matches user requirement: "Mỗi trang hiển thị một số lượng 8 client nhất định, PT có thể chuyển trang và biết tổng số client cùng tổng số trang".
  - 1-based indexing is standard for user interfaces.
  - When `totalElements == 0`, `totalPages = 0`, `items = []`, and no error is raised (fulfilling User Story 5 and FR-007).

### Decision 4: Search & Sort Normalization
- **Decision**:
  - Search keyword: trimmed. Case-insensitive regex matching on `fullName` OR `phone`.
  - Sort fields allowed: `createdAt` (default) and `fullName`.
  - Sort direction allowed: `DESC` (default) and `ASC`.
  - Any unknown sort field gracefully falls back to `createdAt DESC`.
- **Rationale**:
  - Prevents MongoDB injection and invalid property reference exceptions.
  - Ensures predictable sorting order across paginated responses.

---

## 3. Architecture & Constitution Compliance

- **Principle I (Layering)**: Controller delegates to `IGetPtClientsUc`. Repository interface `IClientRepository` defines pagination abstraction. `ClientRepositoryImpl` interacts with `MongoTemplate`.
- **Principle II (Domain Entity vs Persistence Document)**: Repository converts `ClientDocument` to `ClientEntity`. Use case converts `ClientEntity` to `ClientResponse` DTO.
- **Principle III (Use Case Encapsulation)**: Packaged into `x10.zenfit.mvp.service.service.clientService.getPtClientsUc` with `IGetPtClientsUc` and `GetPtClientsUcImpl`.
- **Principle IV (Security & Secrets)**: Authentication principal used for trainerId; no secrets involved.
- **Principle V (Uniform API Envelope)**: Endpoint returns `ApiResponse<PaginatedClientResponse>`.

