# Feature Specification: Training Session Management & PT Calendar

**Feature Branch**: `002-pt-session-calendar`

**Created**: 2026-11-03

**Status**: Draft

**Input**: User description: "Phân tích và đặc tả đầy đủ tính năng quản lý buổi tập PT (Training Session Management) và lịch làm việc PT (PT Calendar) cho dự án ZenFit Backend. Phục vụ việc: PT tạo và quản lý lịch tập với học viên; PT xem lịch làm việc theo ngày, tuần và tháng; ADMIN quản lý và theo dõi lịch tập; Theo dõi trạng thái từng buổi tập; Check-in và xác nhận hoàn thành; Tránh trùng lịch PT hoặc học viên; Liên kết buổi tập với học viên và hợp đồng PT tương ứng. Hệ thống nội bộ dành cho ADMIN và PT."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - PT tạo lịch tập cho học viên gắn với hợp đồng (Priority: P1)

Là một Huấn luyện viên cá nhân (PT) hoặc Quản trị viên (ADMIN), tôi muốn tạo một buổi tập mới cho học viên gắn liền với một hợp đồng PT cụ thể vào một ngày và giờ bắt đầu xác định, để tôi có thể lên kế hoạch huấn luyện rõ ràng cho học viên.

**Why this priority**: Đây là luồng nghiệp vụ cốt lõi đầu tiên. Không có buổi tập được tạo, toàn bộ hệ thống lịch và quy trình vận hành điểm danh không thể hoạt động.

**Independent Test**: Gọi API tạo buổi tập với đầy đủ `clientId`, `trainerId`, `contractId`, `sessionName`, `trainingDate`, `startTime`. Hệ thống trả về bản ghi buổi tập ở trạng thái `SCHEDULED`, `checkInAt = null`, `completedAt = null`, `createdAt` và `updatedAt` được gán tự động.

**Acceptance Scenarios**:

1. **Given** PT đã đăng nhập, học viên `CLIENT001` tồn tại, hợp đồng `CONTRACT001` thuộc về học viên và đang có hiệu lực (`ACTIVE`), **When** PT tạo buổi tập vào ngày `2026-11-10` lúc `08:00:00` với tên "Upper Body", **Then** hệ thống tạo thành công buổi tập với trạng thái `SCHEDULED`.
2. **Given** Hợp đồng `CONTRACT001` thuộc về học viên `CLIENT002`, **When** PT cố gắng tạo buổi tập cho học viên `CLIENT001` nhưng gán `CONTRACT001`, **Then** hệ thống từ chối và thông báo lỗi hợp đồng không thuộc về học viên.
3. **Given** PT A đã có một buổi tập khác vào ngày `2026-11-10` lúc `08:00:00`, **When** PT A cố gắng tạo thêm một buổi tập nữa tại cùng thời điểm, **Then** hệ thống từ chối do xung đột lịch của PT.
4. **Given** Học viên `CLIENT001` đã có lịch tập vào ngày `2026-11-10` lúc `08:00:00`, **When** PT cố gắng xếp lịch cho học viên này tại cùng thời điểm, **Then** hệ thống từ chối do xung đột lịch của học viên.

---

### User Story 2 - PT Calendar: Xem lịch làm việc theo Ngày, Tuần, Tháng (Priority: P1)

Là một Huấn luyện viên (PT) hoặc Quản trị viên (ADMIN), tôi muốn xem lịch tập dưới dạng Lịch (Calendar) theo các chế độ Ngày (DAY), Tuần (WEEK) hoặc Tháng (MONTH) tính từ một mốc ngày chỉ định, để tôi nắm bắt toàn diện khối lượng công việc và kế hoạch tập luyện.

**Why this priority**: Lịch làm việc trực quan là tính năng trọng tâm mà PT sử dụng hàng ngày để điều phối công việc và kiểm tra lịch hẹn.

**Independent Test**: Gửi yêu cầu xem Calendar với `view=WEEK&date=2026-11-04`. Hệ thống tự động xác định khoảng ngày từ Thứ Hai `2026-11-02` đến Chủ Nhật `2026-11-08`, trả về danh sách các buổi tập trong tuần được sắp xếp tăng dần theo `trainingDate` rồi đến `startTime`.

**Acceptance Scenarios**:

1. **Given** PT yêu cầu `view=DAY` và `date=2026-11-05`, **When** hệ thống xử lý, **Then** hệ thống trả về `startDate=2026-11-05`, `endDate=2026-11-05` cùng tất cả các buổi tập diễn ra chính xác trong ngày đó.
2. **Given** PT yêu cầu `view=WEEK` và `date=2026-11-05` (Thứ Năm), **When** hệ thống tính toán khoảng thời gian, **Then** hệ thống tự động gán `startDate=2026-11-02` (Thứ Hai) và `endDate=2026-11-08` (Chủ Nhật) và trả về các buổi tập trong khoảng này.
3. **Given** PT yêu cầu `view=MONTH` và `date=2026-11-15`, **When** hệ thống xử lý, **Then** hệ thống tự động gán `startDate=2026-11-01` và `endDate=2026-11-30` và trả về các buổi tập trong tháng 11/2026.
4. **Given** PT A đang đăng nhập và yêu cầu xem lịch tuần, **When** hệ thống trả về kết quả, **Then** chỉ chứa các buổi tập mà PT A phụ trách, không lẫn buổi tập của PT khác.
5. **Given** ADMIN đang đăng nhập, **When** ADMIN gọi lịch kèm query parameter `trainerId=TRAINER002` hoặc `clientId=CLIENT001`, **Then** hệ thống lọc chính xác theo đối tượng được yêu cầu.

---

### User Story 3 - Vận hành vòng đời buổi tập: Check-in, Hoàn thành, Vắng mặt, Hủy (Priority: P1)

Là một Huấn luyện viên (PT), tôi muốn thực hiện các thao tác ghi nhận diễn biến thực tế của buổi tập (Check-in khi học viên đến phòng tập, Xác nhận hoàn thành sau khi tập xong, Đánh dấu học viên vắng mặt, hoặc Hủy buổi tập khi có sự cố), để hệ thống phản ánh trung thực trạng thái vận hành.

**Why this priority**: Đây là chu trình cốt lõi để theo dõi tiến độ tập luyện và kiểm soát số buổi của học viên.

**Independent Test**: Thực hiện tuần tự các thao tác Check-in (buổi tập cập nhật `checkInAt` thời gian server), Hoàn thành (cập nhật `completedAt` thời gian server và trạng thái `COMPLETED`). Thử nghiệm hủy hoặc hoàn thành lặp lại để xác nhận tính toàn vẹn trạng thái.

**Acceptance Scenarios**:

1. **Given** Buổi tập đang ở trạng thái `SCHEDULED` hoặc `CONFIRMED`, **When** PT thực hiện Check-in, **Then** hệ thống ghi nhận `checkInAt` bằng thời gian hiện tại của máy chủ, cập nhật trạng thái hợp lệ và không cho phép client tự gửi giá trị `checkInAt`.
2. **Given** Buổi tập đã được check-in, **When** PT thực hiện Hoàn thành (complete), **Then** hệ thống ghi nhận `completedAt` bằng thời gian hiện tại của máy chủ, chuyển trạng thái thành `COMPLETED`, cập nhật `updatedAt`.
3. **Given** Học viên không đến tập theo lịch hẹn, **When** PT thực hiện đánh dấu vắng mặt (no-show), **Then** hệ thống chuyển trạng thái buổi tập thành `NO_SHOW` và cập nhật `updatedAt`.
4. **Given** Buổi tập cần hủy bỏ, **When** PT hoặc ADMIN gọi hủy buổi tập, **Then** trạng thái chuyển thành `CANCELLED`, dữ liệu không bị xóa vật lý khỏi cơ sở dữ liệu để phục vụ kiểm toán và thống kê.
5. **Given** Buổi tập đã ở trạng thái `COMPLETED` hoặc `CANCELLED`, **When** người dùng cố gắng Check-in, Hoàn thành hoặc đánh dấu No-show, **Then** hệ thống từ chối và báo lỗi chuyển đổi trạng thái không hợp lệ.

---

### User Story 4 - Quản lý danh sách buổi tập có phân trang và lọc (Priority: P2)

Là một Huấn luyện viên hoặc Quản trị viên, tôi muốn xem danh sách các buổi tập dạng bảng với khả năng phân trang, sắp xếp và lọc đa tiêu chí (theo học viên, PT, hợp đồng, trạng thái, ngày hoặc khoảng ngày), để phục vụ công tác quản lý, tra cứu và tổng hợp số liệu.

**Why this priority**: Dạng xem danh sách phân trang giúp quản trị viên và PT tra cứu lịch sử, tìm kiếm các buổi tập cũ hoặc lập báo cáo vận hành mà không làm quá tải bộ nhớ.

**Independent Test**: Gọi API danh sách buổi tập với phân trang `page=1&size=10` kết hợp bộ lọc `status=COMPLETED&clientId=CLIENT001`. Hệ thống trả về danh sách các buổi tập thỏa mãn kèm thông tin phân trang chuẩn.

**Acceptance Scenarios**:

1. **Given** Người dùng yêu cầu danh sách buổi tập, **When** không truyền tham số phân trang, **Then** hệ thống áp dụng phân trang mặc định hợp lý (ví dụ: `page=1`, `size=10` hoặc `20`), không tải toàn bộ database lên bộ nhớ.
2. **Given** PT lọc danh sách theo `status=COMPLETED`, **When** kết quả trả về, **Then** 100% các buổi tập trong danh sách đều có trạng thái `COMPLETED`.
3. **Given** Không có buổi tập nào thỏa mãn điều kiện lọc, **When** hệ thống phản hồi, **Then** trả về danh sách rỗng (`[]`) cùng mã thành công, không báo lỗi.

---

### User Story 5 - Cập nhật thông tin buổi tập (Priority: P2)

Là một PT hoặc ADMIN, tôi muốn cập nhật lại thông tin buổi tập (đổi ngày tập, đổi giờ tập, đổi tên buổi tập hoặc ghi chú), để điều chỉnh lịch linh hoạt khi học viên có việc bận đột xuất.

**Why this priority**: Giúp tăng tính linh hoạt trong quá trình sắp xếp lịch thực tế giữa PT và học viên.

**Independent Test**: Gọi cập nhật buổi tập đổi giờ tập từ `08:00:00` sang `09:30:00`. Hệ thống kiểm tra trùng lịch mới, nếu không trùng thì lưu thay đổi và cập nhật `updatedAt`.

**Acceptance Scenarios**:

1. **Given** Buổi tập đang ở trạng thái `SCHEDULED`, **When** PT đổi `startTime` sang thời điểm trống khác của cả PT và học viên, **Then** hệ thống cập nhật thành công và ghi nhận `updatedAt`.
2. **Given** PT đổi thời gian buổi tập sang thời điểm mà học viên đã có lịch tập khác, **When** hệ thống kiểm tra, **Then** hệ thống từ chối cập nhật và báo lỗi xung đột lịch học viên.
3. **Given** Buổi tập đã `COMPLETED` hoặc `CANCELLED`, **When** người dùng cố gắng cập nhật ngày hoặc giờ tập, **Then** hệ thống từ chối thao tác.
4. **Given** Client cố tình gửi kèm các trường hệ thống như `id`, `status`, `checkInAt`, `completedAt` trong payload cập nhật, **Then** hệ thống bỏ qua hoặc từ chối, không cho phép ghi đè các trường này qua API cập nhật thông thường.

---

### Edge Cases

- **Xung đột cùng ngày cùng giờ**: Do Domain Entity không có `endTime` hay `duration`, việc kiểm tra xung đột thời gian được thực hiện nghiêm ngặt dựa trên cặp khóa `(trainingDate, startTime)` cho cả PT và học viên. Không cho phép trùng chính xác cùng ngày và cùng giờ bắt đầu.
- **Truy cập lịch của PT khác**: PT không có quyền xem hoặc chỉnh sửa buổi tập của PT khác. Nếu cố tình truy cập qua ID hoặc cố tình truyền `trainerId` khác trên URL, hệ thống từ chối truy cập (403 Forbidden).
- **Phân trang vượt quá số trang**: Khi `page` lớn hơn tổng số trang thực tế, hệ thống trả về danh sách trống cùng tổng số bản ghi đúng, không gây lỗi 500.
- **Định dạng ngày và giờ**: Khi tham số `date` hoặc `startTime` không đúng định dạng chuẩn (`yyyy-MM-dd` cho ngày, `HH:mm:ss` cho giờ), hệ thống trả về lỗi 400 Bad Request kèm thông báo lỗi rõ ràng.
- **Hợp đồng hết hạn hoặc đã đóng**: Khi tạo buổi tập, nếu hợp đồng liên kết không ở trạng thái `ACTIVE` (ví dụ `CANCELLED`, `EXPIRED`), hệ thống từ chối tạo buổi tập.
- **Xóa vật lý vs Hủy logic**: Hệ thống không cung cấp API xóa vật lý bản ghi buổi tập để bảo toàn lịch sử truy vết; mọi hành động hủy đều qua API chuyển trạng thái `CANCELLED`.

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Hệ thống BẮT BUỘC cung cấp API tạo buổi tập mới gắn liền với `clientId`, `trainerId`, `contractId`, `sessionName`, `trainingDate`, `startTime` và `note` tùy chọn.
- **FR-002**: Hệ thống BẮT BUỘC kiểm tra sự tồn tại của học viên, PT và hợp đồng PT trước khi cho phép tạo buổi tập.
- **FR-003**: Hệ thống BẮT BUỘC kiểm tra hợp đồng PT được chỉ định phải thuộc về chính học viên đó và đang ở trạng thái hoạt động (`ACTIVE`).
- **FR-004**: Hệ thống BẮT BUỘC ngăn chặn việc tạo hoặc cập nhật buổi tập nếu PT đã có buổi tập khác tại cùng ngày và cùng giờ bắt đầu (`trainingDate`, `startTime`) mà chưa bị hủy (`status != CANCELLED`).
- **FR-005**: Hệ thống BẮT BUỘC ngăn chặn việc tạo hoặc cập nhật buổi tập nếu học viên đã có buổi tập khác tại cùng ngày và cùng giờ bắt đầu (`trainingDate`, `startTime`) mà chưa bị hủy (`status != CANCELLED`).
- **FR-006**: Trạng thái ban đầu của buổi tập khi vừa tạo BẮT BUỘC là `SCHEDULED`. `checkInAt` và `completedAt` ban đầu BẮT BUỘC là `null`.
- **FR-007**: Hệ thống BẮT BUỘC cung cấp API lấy thông tin chi tiết một buổi tập theo định danh duy nhất (ID) và kiểm tra quyền truy cập của người yêu cầu.
- **FR-008**: Hệ thống BẮT BUỘC cung cấp API danh sách buổi tập hỗ trợ phân trang (`page`, `size`), sắp xếp, và lọc theo `clientId`, `trainerId`, `contractId`, `status`, khoảng ngày `trainingDate`.
- **FR-009**: Hệ thống BẮT BUỘC cung cấp API Calendar chuyên biệt theo các góc nhìn: `DAY`, `WEEK`, `MONTH` dựa trên tham số ngày cơ sở (`date`).
- **FR-010**: Trong chế độ xem `WEEK`, hệ thống BẮT BUỘC tự động tính toán `startDate` là Thứ Hai và `endDate` là Chủ Nhật của tuần chứa ngày được yêu cầu.
- **FR-011**: Trong chế độ xem `MONTH`, hệ thống BẮT BUỘC tự động tính toán `startDate` là ngày đầu tháng và `endDate` là ngày cuối tháng chứa ngày được yêu cầu.
- **FR-012**: Trong chế độ xem `DAY`, hệ thống BẮT BUỘC tự động xác định khoảng thời gian chỉ gồm chính xác ngày đó (`startDate = endDate = date`).
- **FR-013**: Kết quả trả về của Calendar BẮT BUỘC được sắp xếp tăng dần theo `trainingDate`, sau đó tăng dần theo `startTime`.
- **FR-014**: Cấu trúc phản hồi Calendar BẮT BUỘC chứa các trường: `view`, `startDate`, `endDate`, `totalSessions` và danh sách `sessions` sử dụng DTO chuyên dụng cho sự kiện lịch.
- **FR-015**: Hệ thống BẮT BUỘC cung cấp API cập nhật thông tin nghiệp vụ của buổi tập (`sessionName`, `trainingDate`, `startTime`, `note`) và kiểm tra lại xung đột lịch nếu thời gian hoặc đối tượng thay đổi.
- **FR-016**: Hệ thống BẮT BUỘC ngăn chặn việc chỉnh sửa buổi tập nếu buổi tập đã ở trạng thái `COMPLETED` hoặc `CANCELLED`.
- **FR-017**: Hệ thống BẮT BUỘC cung cấp API Check-in; khi thực hiện, hệ thống tự động ghi nhận `checkInAt` bằng thời gian hiện tại của máy chủ, cập nhật trạng thái hợp lệ và cấm ghi đè từ client.
- **FR-018**: Hệ thống BẮT BUỘC cung cấp API Hoàn thành (Complete); khi thực hiện, hệ thống tự động ghi nhận `completedAt` bằng thời gian hiện tại của máy chủ, chuyển trạng thái thành `COMPLETED` và cập nhật `updatedAt`.
- **FR-019**: Hệ thống BẮT BUỘC cung cấp API đánh dấu vắng mặt (No-show); chuyển trạng thái thành `NO_SHOW` và cập nhật `updatedAt`.
- **FR-020**: Hệ thống BẮT BUỘC cung cấp API Hủy buổi tập (Cancel); chuyển trạng thái thành `CANCELLED`, cập nhật `updatedAt` và giữ nguyên bản ghi trong cơ sở dữ liệu để truy vết.
- **FR-021**: Hệ thống BẮT BUỘC chỉ cho phép ADMIN hoặc chính PT phụ trách buổi tập thực hiện các thao tác cập nhật, check-in, complete, no-show, hoặc cancel.
- **FR-022**: [NEEDS CLARIFICATION: Khi check-in buổi tập, trạng thái chuyển sang CONFIRMED hay giữ nguyên SCHEDULED và chỉ cập nhật checkInAt?]
- **FR-023**: [NEEDS CLARIFICATION: Khi hoàn thành buổi tập (COMPLETED) hoặc vắng mặt (NO_SHOW), hệ thống có tự động tăng số buổi đã dùng usedSessions trong PtContractEntity không, hay giữ tách biệt?]
- **FR-024**: [NEEDS CLARIFICATION: Quy tắc xử lý thời lượng buổi tập khi entity không có endTime/duration: Việc kiểm tra trùng lịch chỉ đối chiếu chính xác trainingDate + startTime hay có khoảng đệm tối thiểu?]

---

### Key Entities *(data involvement)*

- **TrainingSession**: Đại diện cho một buổi tập thể hình cá nhân giữa một huấn luyện viên (PT) và một học viên cụ thể.
  - Các thuộc tính cốt lõi: `id`, `clientId`, `trainerId`, `contractId`, `sessionName`, `trainingDate`, `startTime`, `status`, `checkInAt`, `completedAt`, `note`, `createdAt`, `updatedAt`.
  - Không chứa các trường ngoài entity hiện tại như `endTime`, `duration`, `recurrence`.
- **PtContract**: Hợp đồng dịch vụ PT đã ký giữa học viên và phòng tập/PT. Mỗi buổi tập phải gắn với một hợp đồng hợp lệ để kiểm soát quyền lợi tập luyện.
- **Client**: Hồ sơ học viên ZenFit tham gia buổi tập.
- **Trainer (User)**: Tài khoản nhân viên có vai trò PT hoặc cấp quản lý phụ trách hướng dẫn buổi tập.
- **TrainingSessionStatus**: Tập hợp trạng thái định danh tiến trình buổi tập: `SCHEDULED`, `CONFIRMED`, `COMPLETED`, `CANCELLED`, `NO_SHOW`.

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% các yêu cầu tạo hoặc đổi lịch có cùng huấn luyện viên hoặc cùng học viên tại cùng một ngày và giờ bắt đầu đều bị chặn với thông báo lỗi rõ ràng.
- **SC-002**: PT và Quản trị viên có thể xem lịch làm việc theo Ngày, Tuần, Tháng với thời gian phản hồi tức thì dưới 1 giây cho khoảng thời gian 1 tháng dữ liệu.
- **SC-003**: 100% thời gian check-in (`checkInAt`) và thời gian hoàn thành (`completedAt`) được sinh từ đồng hồ hệ thống máy chủ, không có kẽ hở cho client giả mạo thời gian điểm danh.
- **SC-004**: Đảm bảo 100% tính cô lập dữ liệu: PT không thể truy cập, xem hoặc thao tác trên bất kỳ buổi tập nào không thuộc phạm vi phân công của mình.
- **SC-005**: 100% các buổi tập bị hủy đều được lưu vết nguyên vẹn trong hệ thống, không xảy ra tình trạng mất dữ liệu do xóa cứng.
- **SC-006**: Dữ liệu phản hồi của API Calendar tuân thủ tuyệt đối quy ước tuần bắt đầu từ Thứ Hai và kết thúc vào Chủ Nhật, các sự kiện trong cùng một ngày luôn được sắp xếp theo trình tự thời gian tăng dần.

---

## Assumptions

- **A-001**: Khung thời gian làm việc của PT và học viên được chia theo các mốc giờ bắt đầu cụ thể. Khi chưa có trường `endTime` hay `duration`, một buổi tập được coi là xung đột khi và chỉ khi trùng chính xác `trainingDate` và `startTime`.
- **A-002**: Việc trừ số buổi trong hợp đồng PT (`PtContractEntity.usedSessions`) tuân thủ nguyên tắc không tự ý sửa đổi khi chưa có quy định rõ ràng; phiên bản v1 tập trung quản lý vòng đời buổi tập độc lập và đảm bảo hợp đồng hợp lệ khi lên lịch.
- **A-003**: Người dùng đăng nhập được xác thực thông qua JWT Bearer token hiện tại của hệ thống. Thông tin danh tính và vai trò được trích xuất an toàn từ Security Context.
- **A-004**: Vai trò được hỗ trợ trong phạm vi tính năng gồm `ADMIN`, `HEAD_OF_TRAINING`, `TEAM_MANAGER`, `STAFF` (nhóm Quản trị/Quản lý) và `PT` (Huấn luyện viên). Học viên không có quyền truy cập trực tiếp vào API nội bộ này.

---

## Limitations & Proposals *(Phân tích giới hạn & Đề xuất mở rộng)*

### 1. Phân tích giới hạn của Domain Entity hiện tại
- **Thiếu `endTime` / `duration`**: Hiện tại `TrainingSessionEntity` chỉ có `startTime` (kiểu `LocalTime`) và `trainingDate` (kiểu `LocalDate`).
- **Hệ quả**: Hệ thống không thể phát hiện tình trạng chồng lấn thời gian thực tế (ví dụ: Buổi A bắt đầu lúc 08:00 kéo dài 60 phút nhưng Buổi B lại đặt lúc 08:30). Hiện tại chỉ có thể ngăn chặn xung đột trùng khớp chính xác 100% cùng ngày và cùng giờ bắt đầu (`trainingDate` và `startTime`).
- **Thiếu lặp lịch (Recurrence/Repeat)**: PT phải tạo từng buổi tập riêng lẻ, chưa hỗ trợ tạo chuỗi lịch định kỳ hàng tuần.

### 2. Đề xuất giải pháp và lộ trình nâng cấp (Không sửa Domain Entity hiện tại)
- **Giải pháp ngắn hạn (v1)**:
  - Kiểm tra trùng lịch nghiêm ngặt theo `trainingDate` và `startTime`.
  - Áp dụng quy ước chuẩn vận hành phòng tập (mỗi buổi tập mặc định theo ca/slot chuẩn của phòng tập, ví dụ ca 60 phút bắt đầu vào đầu giờ hoặc nửa giờ).
- **Đề xuất nâng cấp (v2)**:
  - Khi có sự phê duyệt bổ sung trường vào Domain Entity: đề xuất thêm `durationMinutes` (ví dụ: 60) hoặc `endTime` (`LocalTime`) để thuật toán kiểm tra overlap thời gian đạt độ chính xác tuyệt đối: `startA < endB && endA > startB`.
