package com.learnhub.automation.tests;

import com.learnhub.automation.base.BaseTest;
import com.learnhub.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TV5 - Toàn: Kịch bản kiểm thử các trường hợp đăng nhập không hợp lệ (Negative Test Cases).
 */
public class LoginInvalidTest extends BaseTest {

    @Test(description = "TC03 - Kiểm tra đăng nhập với mật khẩu sai")
    public void testTC03_LoginWithWrongPassword() {
        LoginPage loginPage = openLoginPage();

        // 1. Nhập email đúng nhưng mật khẩu sai
        loginPage.login("student@learnhub.com", "sai123");

        // 2. Xác minh vẫn ở trang login và hiển thị thông báo lỗi
        String errorMsg = loginPage.getErrorMessage();
        System.out.println(">>> [TC03] Thông báo lỗi nhận được: " + errorMsg);

        Assert.assertTrue(getDriver().getCurrentUrl().contains("/auth/login"),
                "Hệ thống không được chuyển trang khi nhập sai mật khẩu!");
        Assert.assertNotNull(errorMsg, "Phải xuất hiện thông báo lỗi!");
        Assert.assertTrue(errorMsg.length() > 0, "Thông báo lỗi không được rỗng!");
    }

    @Test(description = "TC04 - Kiểm tra đăng nhập với Email không tồn tại trong hệ thống")
    public void testTC04_LoginWithNonExistentEmail() {
        LoginPage loginPage = openLoginPage();

        // 1. Nhập email không tồn tại
        loginPage.login("khongtontai@test.com", "any_password_123");

        // 2. Xác minh hệ thống từ chối đăng nhập và báo lỗi
        String errorMsg = loginPage.getErrorMessage();
        System.out.println(">>> [TC04] Thông báo lỗi nhận được: " + errorMsg);

        Assert.assertTrue(getDriver().getCurrentUrl().contains("/auth/login"),
                "Hệ thống không được đăng nhập tài khoản không tồn tại!");
        Assert.assertNotNull(errorMsg, "Phải có thông báo lỗi từ chối đăng nhập!");
        Assert.assertTrue(errorMsg.length() > 0, "Thông báo lỗi không được rỗng!");
    }

    @Test(description = "TC05 - Kiểm tra khi để trống mật khẩu")
    public void testTC05_LoginWithEmptyPassword() {
        LoginPage loginPage = openLoginPage();

        // 1. Chỉ nhập email, bỏ trống ô password
        loginPage.enterEmail("student@learnhub.com");
        loginPage.clickSubmit();

        // 2. Form không submit thành công, URL vẫn giữ nguyên ở /auth/login
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/auth/login"),
                "Form không được gửi đi khi chưa nhập mật khẩu bắt buộc!");
    }
}
