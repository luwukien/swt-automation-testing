# Demo 3 test đăng nhập

Code: `src/test/java/com/learnhub/automation/tests/LoginDemoTest.java`.
Các test dùng lại `BaseTest`, `LoginPage` và `TestAccount` của baseline.
Mỗi test mở một phiên Chrome mới, không phụ thuộc kết quả test trước.

## Điều kiện trước khi demo

- Java 17, Maven và Chrome hoạt động.
- Website đã chạy tại `http://localhost:8080/learnhub` và kết nối được database.
- Database kiểm thử có dữ liệu seed: tài khoản `student`, email `student@learnhub.com`, mật khẩu `student`, trạng thái `active`.
- `src/main/resources/db.properties` trỏ tới database kiểm thử mà website đang dùng. `BaseTest` gọi dọn dữ liệu test trước và sau suite; không dùng database thật.

## Test case

| ID | Nội dung | Dữ liệu nhập | Các bước | Kết quả mong đợi |
| --- | --- | --- | --- | --- |
| TC01 | Email hợp lệ | `student@learnhub.com` / `student` | Mở login → nhập email và mật khẩu → nhấn Sign in | URL chính xác là `/learnhub/my-enrollments`, hiển thị tiêu đề My Courses và có liên kết Sign out |
| TC02 | Username hợp lệ | `student` / `student` | Mở login → nhập username và mật khẩu → nhấn Sign in | Kết quả giống TC01 |
| TC03 | Mật khẩu sai | `student@learnhub.com` / `wrong_password_123` | Mở login → nhập email và mật khẩu sai → nhấn Sign in | Ở lại `/learnhub/auth/login`, còn form đăng nhập và hiển thị `Incorrect email or password, or your account is locked.` |

Đường dẫn trên dùng context `/learnhub`; nếu chạy context khác, test lấy đường dẫn từ `baseUrl`.

## Chạy đúng 3 test demo

Trong PowerShell tại thư mục dự án:

```powershell
mvn test "-Dtest=LoginDemoTest"
```

Tham số `-Dtest` chọn riêng lớp demo thay cho danh sách trong `testng.xml`; không cần sửa test mẫu của Tech Lead.

Nếu website chạy ở URL khác:

```powershell
mvn test "-Dtest=LoginDemoTest" "-DbaseUrl=http://localhost:8080/learnhub-test"
```

Nếu terminal chưa nhận Maven:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
$env:Path = "$env:JAVA_HOME\bin;C:\Program Files\NetBeans-17\netbeans\java\maven\bin;$env:Path"
```

Chạy có cửa sổ Chrome để demo; thêm `"-Dheadless=true"` khi muốn chạy ngầm.

## Cách trình bày kết quả

1. Giới thiệu ba tình huống trong bảng.
2. Mở `LoginDemoTest.java`, giải thích bước mở trang → nhập thông tin → kiểm tra kết quả.
3. Chạy lệnh, quan sát Chrome thực hiện từng test.
4. Đọc tổng kết trong terminal và báo cáo tại `target/surefire-reports/`.

Khi cả ba đúng kỳ vọng, tổng kết là `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0`.
TC03 vẫn là **Pass** khi ứng dụng từ chối mật khẩu sai đúng như mong đợi.
Không ghi nhận Pass trước khi thực sự chạy. Nếu database mất kết nối, TC03 có thể vẫn hiện thông báo chung; cần TC01 và TC02 thành công để xác nhận luồng đăng nhập hoạt động.
