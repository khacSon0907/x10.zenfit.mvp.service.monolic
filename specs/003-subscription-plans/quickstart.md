# Quickstart Validation Guide: Subscription Plans Management

**Feature**: Admin CRUD Subscription Plans for PT & Client Limits  
**Date**: 2026-11-17  
**Branch**: `003-subscription-plans`

Hướng dẫn chạy thử và kiểm chứng toàn bộ luồng nghiệp vụ API CRUD Gói Dịch Vụ (`SubscriptionPlan`).

---

## 1. Chuẩn bị môi trường

- Backend đang chạy tại `http://localhost:8080`.
- Biến môi trường token:
  - `ADMIN_TOKEN`: Bearer token của tài khoản có quyền `ROLE_ADMIN`.
  - `PT_TOKEN`: Bearer token của tài khoản PT (`ROLE_PT`).

---

## 2. Kịch bản xác thực (Validation Scenarios)

### Kịch bản 1: Admin tạo gói Free mặc định (Gói khởi nghiệp)
Admin quy định mỗi PT mới đăng ký được quản lý tối đa 5 học viên miễn phí:

```bash
curl -X POST "http://localhost:8080/api/subscription-plans" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "FREE_PT",
    "name": "Gói Khởi Nghiệp PT (Miễn phí)",
    "description": "Gói mặc định cho mọi huấn luyện viên, quản lý tối đa 5 học viên",
    "price": 0,
    "durationDays": 3650,
    "maxStudents": 5,
    "maxTrainingSessions": 0,
    "status": "ACTIVE",
    "isDefault": true,
    "displayOrder": 1
  }'
```

**Kỳ vọng**:
- HTTP Status: `201 Created`
- Response trả về gói có `isDefault = true`, `maxStudents = 5`, `code = "FREE_PT"`.

---

### Kịch bản 2: Admin tạo gói trả phí (Pro Plan)
Admin tạo thêm gói trả phí cho PT có đông học viên (tối đa 50 học viên):

```bash
curl -X POST "http://localhost:8080/api/subscription-plans" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PRO_PT_MONTHLY",
    "name": "Gói PT Chuyên Nghiệp 1 Tháng",
    "description": "Nâng hạn mức quản lý lên 50 học viên, không giới hạn lịch tập",
    "price": 199000,
    "durationDays": 30,
    "maxStudents": 50,
    "maxTrainingSessions": 100,
    "status": "ACTIVE",
    "isDefault": false,
    "displayOrder": 2
  }'
```

**Kỳ vọng**:
- HTTP Status: `201 Created`
- Response trả về gói mới với `price = 199000`.

---

### Kịch bản 3: PT xem danh sách gói để lựa chọn nâng cấp

```bash
curl -X GET "http://localhost:8080/api/subscription-plans" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- Trả về danh sách gồm các gói `ACTIVE` (Gói Free, Gói Pro), sắp xếp theo `displayOrder`.

---

### Kịch bản 4: Admin xem danh sách quản trị kèm phân trang và lọc trạng thái

```bash
curl -X GET "http://localhost:8080/api/subscription-plans/admin?page=0&size=10&status=ACTIVE" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- Trả về đối tượng `PaginatedResult` gồm `items`, `totalItems`, `totalPages`, `currentPage`.

---

### Kịch bản 5: Admin điều chỉnh hạn mức học viên gói Free từ 5 lên 10 học viên

```bash
# Thay {FREE_PLAN_ID} bằng ID trả về từ Kịch bản 1
curl -X PUT "http://localhost:8080/api/subscription-plans/{FREE_PLAN_ID}" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gói Khởi Nghiệp PT (Cập nhật)",
    "description": "Mở rộng ưu đãi quản lý tối đa 10 học viên",
    "price": 0,
    "durationDays": 3650,
    "maxStudents": 10,
    "maxTrainingSessions": 0,
    "isDefault": true,
    "displayOrder": 1
  }'
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- `maxStudents` đổi thành `10`, `updatedAt` được cập nhật mới.

---

### Kịch bản 6: Kiểm tra bảo vệ - Thử xóa gói Default (Edge case)

```bash
curl -X DELETE "http://localhost:8080/api/subscription-plans/{FREE_PLAN_ID}" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `400 Bad Request`
- Error Code: `SUB_PLAN_400_DEFAULT`
- Message: `Không thể xóa hoặc vô hiệu hóa gói dịch vụ mặc định.`

---

### Kịch bản 7: Admin chuyển đổi trạng thái gói sang INACTIVE

```bash
# Thay {PRO_PLAN_ID} bằng ID trả về từ Kịch bản 2
curl -X PATCH "http://localhost:8080/api/subscription-plans/{PRO_PLAN_ID}/status" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INACTIVE"
  }'
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- `status` đổi thành `INACTIVE`. Khi PT gọi `GET /api/subscription-plans`, gói này sẽ không còn xuất hiện trong danh sách.

