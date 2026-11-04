# Quickstart Validation Guide: PT Client List Pagination, Search, and Filtering

**Feature**: `001-pt-client-list`  
**Date**: 2026-11-01  
**Status**: Ready for Implementation

---

## 1. Prerequisites

1. MongoDB and Redis instances running locally or in development environment.
2. Spring Boot application running on `http://localhost:8080`.
3. An authenticated PT account with a valid Bearer JWT Token (`PT_TOKEN`).
4. At least 15 clients in the database:
   - 10 clients assigned to `PT_TOKEN`'s trainer ID (`pt-101`), including:
     - 1 client with name "Nguyễn Văn Minh", phone "0987654321", status "ACTIVE".
     - 1 client with name "Trần Thị Lan", phone "0912345678", status "INACTIVE".
   - 5 clients assigned to another trainer (`pt-202`).

---

## 2. Validation Scenarios

### Scenario 1: First Page Pagination (Default 8 items)
Verify default page size of 8 items, total count, total pages, and isolation.

```bash
curl -X GET "http://localhost:8080/api/clients/my-clients?page=1" \
  -H "Authorization: Bearer $PT_TOKEN" \
  -H "Accept: application/json"
```

**Expected Result**:
- Status: `200 OK`
- `data.page`: `1`
- `data.size`: `8`
- `data.items.length`: `8`
- `data.totalElements`: `10`
- `data.totalPages`: `2`
- `data.hasNext`: `true`
- `data.hasPrevious`: `false`
- All items in `data.items` have `assignedTrainerId: "pt-101"`. None belong to `pt-202`.

---

### Scenario 2: Second Page Pagination
Verify page navigation to page 2.

```bash
curl -X GET "http://localhost:8080/api/clients/my-clients?page=2" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Expected Result**:
- Status: `200 OK`
- `data.page`: `2`
- `data.items.length`: `2` (the remaining 2 clients of the total 10)
- `data.hasNext`: `false`
- `data.hasPrevious`: `true`

---

### Scenario 3: Search by Name or Phone
Search for "Minh" or "0987".

```bash
curl -X GET "http://localhost:8080/api/clients/my-clients?search=Minh" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Expected Result**:
- Status: `200 OK`
- Only clients matching "Minh" in `fullName` are returned.
- `data.totalElements`: Count of matching clients (e.g., `1`).
- `data.totalPages`: `1`.

---

### Scenario 4: Filter by Status & Sort
Filter by `status=ACTIVE` and sort by `fullName` ascending.

```bash
curl -X GET "http://localhost:8080/api/clients/my-clients?status=ACTIVE&sortBy=fullName&sortDirection=ASC" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Expected Result**:
- Status: `200 OK`
- All items have `status: "ACTIVE"`.
- Items are sorted alphabetically by `fullName`.

---

### Scenario 5: Empty Result Handling (Graceful Empty State)
Search for a non-existent keyword.

```bash
curl -X GET "http://localhost:8080/api/clients/my-clients?search=NotFoundString999" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Expected Result**:
- Status: `200 OK` (No 404 or 500 error!)
- `data.items`: `[]` (empty array)
- `data.totalElements`: `0`
- `data.totalPages`: `0`
- `data.hasNext`: `false`
- `data.hasPrevious`: `false`

---

## 3. Related Artifacts

- Specification: [`specs/001-pt-client-list/spec.md`](../spec.md)
- Data Model: [`specs/001-pt-client-list/data-model.md`](data-model.md)
- API Contract: [`specs/001-pt-client-list/contracts/pt-client-list-api.json`](contracts/pt-client-list-api.json)

