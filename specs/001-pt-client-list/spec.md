# Feature Specification: PT Client List Pagination, Search, and Filtering

**Feature Branch**: `001-pt-client-list`

**Created**: 2026-11-01

**Status**: Draft

**Input**: User description: "PT có thể xem danh sách client của mình theo từng trang. Mỗi trang hiển thị một số lượng 8 client nhất định, PT có thể chuyển trang và biết tổng số client cùng tổng số trang. PT chỉ thấy client được gán cho mình, không thấy client của PT khác. PT có thể tìm client theo tên hoặc số điện thoại, lọc theo trạng thái client, và sắp xếp theo ngày tạo hoặc tên. Khi không có client nào khớp thì trả về danh sách rỗng, không báo lỗi."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Xem danh sách khách hàng phân trang (Priority: P1)

Là một Huấn luyện viên cá nhân (PT), tôi muốn xem danh sách khách hàng được phân công cho mình dưới dạng từng trang (mỗi trang 8 khách hàng) cùng thông tin tổng số khách hàng và tổng số trang, để tôi có thể nắm bắt và quản lý số lượng học viên một cách khoa học, thuận tiện.

**Why this priority**: Đây là luồng người dùng cốt lõi của tính năng. Nếu không có danh sách phân trang hiển thị đúng 8 khách hàng mỗi trang, tính năng chưa thể mang lại giá trị vận hành cơ bản cho PT.

**Independent Test**: Đăng nhập với tài khoản PT có 18 khách hàng, yêu cầu trang 1; hệ thống hiển thị 8 khách hàng đầu tiên cùng thông tin tổng số 18 khách hàng và tổng số 3 trang. Chuyển sang trang 2 và trang 3 hiển thị các khách hàng tiếp theo tương ứng.

**Acceptance Scenarios**:

1. **Given** PT đã đăng nhập và được gán 20 khách hàng, **When** PT truy cập danh sách khách hàng ở trang 1, **Then** hệ thống trả về chính xác 8 khách hàng của trang 1, tổng số khách hàng là 20, tổng số trang là 3, và trang hiện tại là 1.
2. **Given** PT đang ở trang 1 của danh sách, **When** PT chọn chuyển sang trang 2, **Then** hệ thống trả về 8 khách hàng tiếp theo (từ khách hàng thứ 9 đến 16), thông tin phân trang cập nhật trang hiện tại là 2.
3. **Given** PT chuyển đến trang cuối cùng (trang 3), **When** hệ thống tải dữ liệu, **Then** danh sách hiển thị 4 khách hàng còn lại, tổng số khách hàng vẫn là 20 và tổng số trang là 3.

---

### User Story 2 - Bảo mật và cô lập dữ liệu theo PT (Priority: P1)

Là một Huấn luyện viên cá nhân (PT), tôi muốn chỉ nhìn thấy các khách hàng được phân công trực tiếp cho tôi, để bảo mật thông tin cá nhân của khách hàng và tránh can thiệp vào học viên của huấn luyện viên khác.

**Why this priority**: Bảo mật và phân quyền dữ liệu là tiêu chuẩn bắt buộc. Dữ liệu giữa các PT phải được cách ly hoàn toàn để đảm bảo tính riêng tư và trách nhiệm công việc.

**Independent Test**: Đăng nhập với tài khoản PT A, lấy danh sách khách hàng; đảm bảo 100% khách hàng trả về đều thuộc quyền quản lý của PT A. Không có bất kỳ khách hàng nào của PT B xuất hiện trong danh sách hay trong tổng số đếm.

**Acceptance Scenarios**:

1. **Given** Hệ thống có 50 khách hàng, trong đó 10 khách hàng gán cho PT A và 40 khách hàng gán cho các PT khác, **When** PT A yêu cầu danh sách khách hàng của mình, **Then** hệ thống chỉ trả về các khách hàng thuộc PT A và báo tổng số khách hàng là 10.
2. **Given** PT A cố ý yêu cầu danh sách kèm định danh của PT B, **When** hệ thống xử lý, **Then** hệ thống từ chối hoặc chỉ lấy dữ liệu theo định danh được xác thực của PT A, không để lộ khách hàng của PT B.

---

### User Story 3 - Tìm kiếm khách hàng theo tên hoặc số điện thoại (Priority: P2)

Là một PT, tôi muốn tìm kiếm nhanh học viên của mình bằng cách nhập một phần họ tên hoặc số điện thoại, để tôi có thể liên hệ và kiểm tra tiến độ của học viên ngay lập tức mà không phải lật qua nhiều trang.

**Why this priority**: Khi danh sách khách hàng tăng lên, tìm kiếm là công cụ thiết yếu giúp PT tiết kiệm thời gian thao tác.

**Independent Test**: PT nhập chuỗi tìm kiếm "0912" hoặc "Minh", hệ thống lọc ra chính xác các khách hàng của PT có tên chứa "Minh" hoặc số điện thoại chứa "0912", số trang và tổng số khách hàng được tính lại theo kết quả tìm kiếm.

**Acceptance Scenarios**:

1. **Given** PT có các khách hàng "Nguyễn Văn Minh" và "Lê Minh Tuấn", **When** PT nhập từ khóa "Minh" vào ô tìm kiếm, **Then** hệ thống hiển thị danh sách chỉ gồm những khách hàng có tên chứa từ "Minh" (không phân biệt chữ hoa, chữ thường).
2. **Given** PT có khách hàng với số điện thoại "0987654321", **When** PT tìm kiếm với từ khóa "6543", **Then** hệ thống trả về đúng khách hàng có số điện thoại tương ứng.
3. **Given** PT áp dụng từ khóa tìm kiếm, **When** kết quả trả về, **Then** thông tin tổng số khách hàng và tổng số trang phản ánh đúng số lượng bản ghi thỏa mãn điều kiện tìm kiếm, mỗi trang tối đa 8 bản ghi.

---

### User Story 4 - Lọc theo trạng thái và sắp xếp danh sách (Priority: P2)

Là một PT, tôi muốn lọc khách hàng theo trạng thái (như đang tập luyện, tạm dừng, hết hạn) và sắp xếp theo ngày tạo hoặc theo tên, để tôi có thể ưu tiên chăm sóc các học viên mới hoặc lọc nhóm học viên cần theo dõi.

**Why this priority**: Giúp PT phân loại công việc và quản lý tiến độ học viên theo thứ tự ưu tiên rõ ràng.

**Independent Test**: PT chọn bộ lọc trạng thái "Đang hoạt động" và sắp xếp theo "Ngày tạo giảm dần" (mới nhất trước); danh sách chỉ hiển thị khách hàng đang hoạt động với khách hàng mới tham gia hiển thị đầu tiên.

**Acceptance Scenarios**:

1. **Given** PT có cả khách hàng "ACTIVE" và "INACTIVE", **When** PT chọn lọc trạng thái "ACTIVE", **Then** danh sách chỉ hiển thị những khách hàng có trạng thái "ACTIVE", không chứa khách hàng "INACTIVE".
2. **Given** PT yêu cầu sắp xếp theo ngày tạo giảm dần, **When** danh sách tải xong, **Then** khách hàng được tạo gần nhất hiển thị ở vị trí đầu tiên của trang 1.
3. **Given** PT chọn sắp xếp theo tên theo thứ tự bảng chữ cái A-Z, **When** danh sách hiển thị, **Then** các khách hàng được sắp xếp tăng dần theo họ và tên.

---

### User Story 5 - Xử lý danh sách rỗng an toàn khi không có kết quả khớp (Priority: P3)

Là một PT, khi tôi tìm kiếm hoặc lọc với điều kiện không có học viên nào thỏa mãn, tôi muốn nhận được giao diện danh sách rỗng thân thiện (thay vì thông báo lỗi hệ thống), để tôi hiểu rõ là không có dữ liệu phù hợp và có thể xóa bộ lọc.

**Why this priority**: Cải thiện trải nghiệm người dùng, tránh gây hoang mang hoặc hiểu nhầm rằng hệ thống gặp sự cố.

**Independent Test**: Nhập từ khóa tìm kiếm không tồn tại như "xyz9999", hệ thống trả về danh sách rỗng, `totalClients = 0`, `totalPages = 0`, HTTP trạng thái thành công, giao diện hiển thị thông điệp "Không tìm thấy khách hàng nào".

**Acceptance Scenarios**:

1. **Given** PT tìm kiếm từ khóa "ChuỗiKhôngTồnTại", **When** hệ thống thực hiện truy vấn, **Then** hệ thống trả về danh sách trống, tổng số bản ghi là 0, tổng số trang là 0, không phát sinh mã lỗi.
2. **Given** Một PT mới được tạo tài khoản và chưa được phân công khách hàng nào, **When** PT truy cập màn hình danh sách, **Then** hệ thống hiển thị danh sách trống cùng thông điệp hướng dẫn rõ ràng mà không báo lỗi.

---

### Edge Cases

- **Truy vấn trang vượt quá tổng số trang**: Khi người dùng yêu cầu số trang lớn hơn tổng số trang hiện có (ví dụ: có 3 trang nhưng yêu cầu trang 10), hệ thống trả về danh sách rỗng kèm thông tin phân trang chính xác (`totalClients = 20`, `totalPages = 3`, `page = 10`), không gây lỗi ứng dụng.
- **Số trang hoặc kích thước trang không hợp lệ**: Nếu tham số trang nhỏ hơn 1, hệ thống tự động quy về trang 1. Kích thước trang mặc định cố định là 8 khách hàng mỗi trang.
- **Từ khóa tìm kiếm có khoảng trắng thừa hoặc ký tự đặc biệt**: Hệ thống tự động cắt tỉa khoảng trắng ở hai đầu (trim) và xử lý ký tự an toàn trước khi so khớp, tránh lỗi phân tích cú pháp truy vấn.
- **PT đổi bộ lọc hoặc từ khóa khi đang ở trang lớn hơn 1**: Khi thay đổi tiêu chí lọc hoặc từ khóa tìm kiếm khiến tổng số trang giảm xuống, hệ thống tự động đưa người dùng về lại trang 1 để tránh rơi vào trang trống.
- **Khách hàng không có số điện thoại**: Trường hợp dữ liệu khách hàng chưa cập nhật số điện thoại, việc tìm kiếm theo số điện thoại vẫn hoạt động bình thường mà không gây lỗi tham chiếu rỗng.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Hệ thống BẮT BUỘC chỉ hiển thị các khách hàng được phân công trực tiếp cho PT đang đăng nhập (`assignedTrainerId`), tuyệt đối không để lộ dữ liệu của PT khác.
- **FR-002**: Hệ thống BẮT BUỘC hỗ trợ phân trang danh sách với kích thước cố định là 8 khách hàng trên mỗi trang theo mặc định.
- **FR-003**: Dữ liệu phân trang trả về BẮT BUỘC bao gồm: danh sách khách hàng của trang hiện tại, số trang hiện tại (`page`), kích thước trang (`size`), tổng số khách hàng (`totalElements`), và tổng số trang (`totalPages`).
- **FR-004**: Hệ thống BẮT BUỘC hỗ trợ tìm kiếm khách hàng theo chuỗi ký tự kết hợp: tìm khớp một phần (partial match) không phân biệt hoa thường theo họ tên (`fullName`) HOẶC theo số điện thoại (`phone`).
- **FR-005**: Hệ thống BẮT BUỘC hỗ trợ lọc danh sách theo trạng thái khách hàng (`status`, ví dụ: ACTIVE, INACTIVE, POTENTIAL).
- **FR-006**: Hệ thống BẮT BUỘC hỗ trợ sắp xếp danh sách theo tiêu chí ngày tạo (`createdAt`) hoặc tên (`fullName`), với chiều tăng dần (ASC) hoặc giảm dần (DESC). Mặc định sắp xếp theo ngày tạo mới nhất (DESC).
- **FR-007**: Khi không có khách hàng nào thỏa mãn điều kiện tìm kiếm hoặc lọc, hệ thống BẮT BUỘC trả về danh sách rỗng với mã phản hồi thành công (200 OK), `totalElements = 0`, `totalPages = 0`, tuyệt đối KHÔNG ném lỗi hay mã trạng thái thất bại.
- **FR-008**: Nếu người dùng nhập số trang không hợp lệ (ví dụ: nhỏ hơn 1), hệ thống BẮT BUỘC tự động điều chỉnh về trang 1.
- **FR-009**: Thông tin mỗi khách hàng trong danh sách BẮT BUỘC bao gồm các trường cơ bản: mã định danh, họ tên, số điện thoại, email, giới tính, trạng thái, ảnh đại diện (nếu có), ngày tham gia và ngày tạo.
- **FR-010**: Mọi yêu cầu truy cập danh sách khách hàng BẮT BUỘC phải được xác thực người dùng hợp lệ có vai trò Huấn luyện viên (PT) hoặc quyền quản lý tương đương.

### Key Entities *(include if feature involves data)*

- **Client (`ClientEntity`)**: Đại diện cho học viên/khách hàng được quản lý trong hệ thống.
  - Các thuộc tính chính: Mã định danh (`id`), họ tên (`fullName`), số điện thoại (`phone`), email (`email`), giới tính (`gender`), ngày sinh (`dateOfBirth`), trạng thái (`status`), ảnh đại diện (`avatarUrl`), huấn luyện viên phụ trách (`assignedTrainerId`), chiều cao (`height`), cân nặng ban đầu (`initialWeight`), ghi chú (`notes`), ngày tham gia (`joinedDate`), thời gian tạo (`createdAt`), thời gian cập nhật (`updatedAt`).
- **PaginatedClientResult**: Đại diện cho cấu trúc kết quả phân trang trả về cho người dùng.
  - Các thuộc tính: Danh sách khách hàng (`items`), trang hiện tại (`page`), số lượng bản ghi trên trang (`size`), tổng số bản ghi (`totalElements`), tổng số trang (`totalPages`), cờ báo có trang tiếp theo (`hasNext`), cờ báo có trang trước đó (`hasPrevious`).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% kết quả trả về trong danh sách khách hàng của PT chỉ chứa đúng các khách hàng được gán cho PT đó, không có bất kỳ trường hợp rò rỉ dữ liệu chéo giữa các PT.
- **SC-002**: Thời gian phản hồi khi tải danh sách 8 khách hàng (bao gồm tìm kiếm và đếm tổng số trang) đạt dưới 1.0 giây trong điều kiện mạng tiêu chuẩn với cơ sở dữ liệu có hàng chục nghìn học viên.
- **SC-003**: PT có thể hoàn tất việc tìm kiếm một học viên cụ thể theo tên hoặc số điện thoại trong vòng dưới 3 giây qua giao diện.
- **SC-004**: Tỷ lệ truy vấn tìm kiếm/lọc trả về kết quả rỗng không gây ra bất kỳ lỗi hệ thống nào (tỷ lệ lỗi 0% đối với trường hợp không tìm thấy bản ghi).

## Assumptions

- PT đã đăng nhập thành công vào hệ thống và mã định danh của PT (`trainerId`) được trích xuất an toàn từ phiên đăng nhập/token xác thực, không dựa hoàn toàn vào tham số do client gửi lên.
- Chỉ số trang (`page`) trong giao diện người dùng và API bắt đầu từ 1 (1-based index).
- Mặc định sắp xếp danh sách là theo ngày tạo mới nhất (`createdAt` giảm dần) khi người dùng không chọn tiêu chí sắp xếp cụ thể.
- Kích thước trang cố định là 8 khách hàng mỗi trang để đồng bộ với thiết kế giao diện thẻ/lưới (grid layout) của ứng dụng Frontend.
- Tìm kiếm theo số điện thoại hỗ trợ tìm theo các số đuôi hoặc đoạn số liên tiếp.

