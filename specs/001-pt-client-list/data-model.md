# Data Model: PT Client List Pagination, Search, and Filtering

**Feature**: `001-pt-client-list`  
**Date**: 2026-11-01  
**Status**: Ready for Implementation

---

## 1. Domain Entities & Persistence Documents

### `ClientEntity` (`common.domain.entities.client.ClientEntity`)
Domain representation of a client/trainee. Independent of persistence details.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `String` | Unique client identifier (UUID / Mongo ID) |
| `fullName` | `String` | Full name of the client |
| `gender` | `String` | Gender ("MALE", "FEMALE", "OTHER") |
| `dateOfBirth` | `LocalDate` | Date of birth |
| `phone` | `String` | Contact phone number |
| `email` | `String` | Contact email address |
| `address` | `String` | Residential address |
| `avatarUrl` | `String` | Cloudinary URL for avatar |
| `status` | `ClientStatus` | Client status enum (`ACTIVE`, `INACTIVE`, `POTENTIAL`) |
| `assignedTrainerId` | `String` | ID of the PT managing this client (**Isolation Key**) |
| `height` | `Double` | Initial height in cm |
| `initialWeight` | `Double` | Initial weight in kg |
| `notes` | `String` | PT notes about the client |
| `joinedDate` | `LocalDate` | Date client joined ZenFit |
| `createdAt` | `LocalDateTime` | Record creation timestamp |
| `updatedAt` | `LocalDateTime` | Record last updated timestamp |

### `ClientDocument` (`infrastructure.client.db.mongodb.ClientDocument`)
MongoDB persistence document stored in the `clients` collection.
- Mapped 1:1 with `ClientEntity` via `ClientGoalMapper` / `ClientMapper`.
- Index recommendations:
  - `{ "assignedTrainerId": 1, "createdAt": -1 }` (Primary compound index for PT list)
  - `{ "assignedTrainerId": 1, "status": 1, "createdAt": -1 }` (Filtered compound index)
  - `{ "fullName": "text", "phone": "text" }` or regex search on indexed prefix.

---

## 2. Request & Response DTOs

### `GetPtClientsQuery` (`service.clientService.getPtClientsUc.GetPtClientsQuery`)
Input parameters passed to the use case.

| Field | Type | Default | Validation & Constraints |
| :--- | :--- | :--- | :--- |
| `trainerId` | `String` | Mandatory | Extracted from `SecurityContext` (non-null) |
| `page` | `int` | `1` | Must be `>= 1`. Default `1` if null/invalid |
| `size` | `int` | `8` | Fixed at `8` per business requirement |
| `search` | `String` | `null` | Optional keyword (trimmed). Matched against `fullName` OR `phone` |
| `status` | `ClientStatus` | `null` | Optional status filter (`ACTIVE`, `INACTIVE`, `POTENTIAL`) |
| `sortBy` | `String` | `"createdAt"` | Allowed values: `"createdAt"`, `"fullName"` |
| `sortDirection` | `String` | `"DESC"` | Allowed values: `"ASC"`, `"DESC"` |

### `PaginatedClientResponse` (`service.clientService.getPtClientsUc.PaginatedClientResponse`)
Pagination wrapper returned in `ApiResponse<PaginatedClientResponse>`.

```json
{
  "items": [
    {
      "id": "6723abc...",
      "fullName": "Nguyễn Văn Minh",
      "gender": "MALE",
      "dateOfBirth": "1995-08-15",
      "phone": "0987654321",
      "email": "minh.nguyen@example.com",
      "address": "123 Đường 3/2, Quận 10, TP.HCM",
      "avatarUrl": "https://res.cloudinary.com/...",
      "status": "ACTIVE",
      "assignedTrainerId": "pt-101",
      "height": 175.0,
      "initialWeight": 78.5,
      "notes": "Mục tiêu giảm 5kg mỡ",
      "joinedDate": "2026-10-01",
      "createdAt": "2026-10-01T08:30:00",
      "updatedAt": "2026-10-25T14:20:00"
    }
  ],
  "page": 1,
  "size": 8,
  "totalElements": 20,
  "totalPages": 3,
  "hasNext": true,
  "hasPrevious": false
}
```

---

## 3. Mathematical & Pagination Logic

1. **Offset calculation**: `skip = (page - 1) * size`.
2. **Total Pages calculation**:
   - If `totalElements == 0`: `totalPages = 0`.
   - Else: `totalPages = (int) Math.ceil((double) totalElements / size)`.
3. **Empty Result Rule**:
   - If `totalElements == 0` or `skip >= totalElements`:
     - `items = []`
     - `totalPages = (int) Math.ceil((double) totalElements / size)` (0 when totalElements == 0)
     - `hasNext = false`
     - `hasPrevious = page > 1`
     - Response status: **200 OK** (No error raised).

