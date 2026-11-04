# Quickstart Validation Guide: Training Session Management & PT Calendar

**Feature**: `002-pt-session-calendar`  
**Date**: 2026-11-03  
**Status**: Ready for Implementation  

---

## 1. Prerequisites

1. MongoDB and Redis instances running.
2. Spring Boot application running on `http://localhost:8080`.
3. Có sẵn các tài khoản thử nghiệm:
   - Một tài khoản PT: Bearer token `$PT_TOKEN` (`trainerId = pt-001`).
   - Một tài khoản PT khác (`trainerId = pt-002`).
   - Một tài khoản Quản trị: Bearer token `$ADMIN_TOKEN`.
4. Một học viên `$CLIENT_ID` (`CLIENT001`) và một hợp đồng PT có hiệu lực `$CONTRACT_ID` (`CONTRACT001`) của học viên này (`status = ACTIVE`).

---

## 2. Validation Scenarios (Kịch bản kiểm thử API qua cURL)

### Scenario 1: Tạo buổi tập mới thành công

```bash
curl -X POST "http://localhost:8080/api/training-sessions" \
  -H "Authorization: Bearer $PT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "CLIENT001",
    "contractId": "CONTRACT001",
    "sessionName": "Upper Body Workout",
    "trainingDate": "2026-11-10",
    "startTime": "08:00:00",
    "note": "Tập trung ngực và vai"
  }'
```

**Kỳ vọng**:
- HTTP Status: `201 Created`
- `data.status`: `"SCHEDULED"`
- `data.trainerId`: `"pt-001"` (tự động nhận từ PT token)
- `data.checkInAt`: `null`
- `data.completedAt`: `null`
- `data.id`: Không null (chuỗi định danh mới)

---

### Scenario 2: Chặn xung đột lịch của PT (Cùng ngày cùng giờ)

Thực hiện tạo lại một buổi tập khác cho cùng PT vào đúng `2026-11-10` lúc `08:00:00`.

```bash
curl -X POST "http://localhost:8080/api/training-sessions" \
  -H "Authorization: Bearer $PT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "CLIENT002",
    "contractId": "CONTRACT002",
    "sessionName": "Leg Day",
    "trainingDate": "2026-11-10",
    "startTime": "08:00:00"
  }'
```

**Kỳ vọng**:
- HTTP Status: `409 Conflict` (hoặc `400 Bad Request`)
- Mã lỗi: `SESSION.CONFLICT`
- Message: Báo lỗi PT đã có lịch tập tại thời điểm này.

---

### Scenario 3: Xem PT Calendar theo Tuần (WEEK)

Gửi yêu cầu xem lịch tuần chứa ngày `2026-11-10` (Thứ Ba).

```bash
curl -X GET "http://localhost:8080/api/training-sessions/calendar?view=WEEK&date=2026-11-10" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- `data.view`: `"WEEK"`
- `data.startDate`: `"2026-11-09"` (Thứ Hai)
- `data.endDate`: `"2026-11-15"` (Chủ Nhật)
- `data.sessions`: Chứa danh sách các buổi tập của PT trong tuần, sắp xếp tăng dần theo `trainingDate` rồi `startTime`.

---

### Scenario 4: Điểm danh (Check-in) buổi tập

```bash
curl -X PATCH "http://localhost:8080/api/training-sessions/$SESSION_ID/check-in" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- `data.checkInAt`: Được cập nhật thời gian server hiện tại (không null).
- `data.status`: `"CONFIRMED"`.

---

### Scenario 5: Xác nhận Hoàn thành (Complete) buổi tập

```bash
curl -X PATCH "http://localhost:8080/api/training-sessions/$SESSION_ID/complete" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `200 OK`
- `data.completedAt`: Được cập nhật thời gian server hiện tại (không null).
- `data.status`: `"COMPLETED"`.

---

### Scenario 6: Chặn thao tác sau khi buổi tập đã hoàn thành

Cố tình gọi hủy hoặc check-in lại buổi tập đã `COMPLETED`.

```bash
curl -X PATCH "http://localhost:8080/api/training-sessions/$SESSION_ID/cancel" \
  -H "Authorization: Bearer $PT_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `400 Bad Request`
- Mã lỗi: `SESSION.INVALID_STATUS`
- Message: Báo lỗi không thể hủy buổi tập đã hoàn thành.

---

### Scenario 7: Cô lập dữ liệu giữa các PT

PT khác (`$OTHER_PT_TOKEN`) cố tình lấy chi tiết hoặc cập nhật buổi tập của `$PT_TOKEN`.

```bash
curl -X GET "http://localhost:8080/api/training-sessions/$SESSION_ID" \
  -H "Authorization: Bearer $OTHER_PT_TOKEN"
```

**Kỳ vọng**:
- HTTP Status: `403 Forbidden`
- Mã lỗi: `SESSION.ACCESS_DENIED`.

