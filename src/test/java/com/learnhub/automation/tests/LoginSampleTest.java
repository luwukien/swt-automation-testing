package com.learnhub.automation.tests;

import com.learnhub.automation.base.BaseTest;
import com.learnhub.automation.model.TestAccount;
import com.learnhub.automation.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Bộ kiểm thử mẫu (Sample Test Suite) do TV3 xây dựng làm mẫu tham chiếu cho
 * TV4 và TV5.
 * Kế thừa trực tiếp từ BaseTest để tái sử dụng toàn bộ vòng đời WebDriver và
 * tiện ích.
 */
public class LoginSampleTest extends BaseTest {

    @DataProvider(name = "defaultRolesProvider")
    public Object[][] provideDefaultRoles() {
        return new Object[][] {
                { "Admin" },
                { "Manager" },
                { "Expert" },
                { "Student" }
        };
    }

    @Test(dataProvider = "defaultRolesProvider", description = "Kiểm tra đăng nhập thành công với 4 vai trò mặc định (Admin, Manager, Expert, Student)")
    public void testLoginAsDefaultRoles(String roleName) {
        LoginPage loginPage = openLoginPage();

        // 1 dòng code duy nhất để đăng nhập theo vai trò
        loginPage.loginAs(roleName);

        // Chờ chuyển hướng hoàn tất sau khi đăng nhập thành công
        new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/login")));

        // Sau khi đăng nhập thành công, URL sẽ rời khỏi trang đăng nhập /auth/login
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertFalse(currentUrl.endsWith("/auth/login"),
                "Tài khoản " + roleName + " phải đăng nhập thành công và chuyển trang!");
    }

    @Test(description = "Kiểm tra thông báo lỗi khi nhập sai mật khẩu")
    public void testLoginWithInvalidPassword() {
        LoginPage loginPage = openLoginPage();

        loginPage.login("admin@learnhub.com", "wrong_password_123");

        // Xác nhận hiển thị thông báo lỗi
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertNotNull(errorMsg, "Phải hiển thị thông báo lỗi trên màn hình!");
        Assert.assertFalse(errorMsg.isEmpty(), "Nội dung lỗi không được rỗng!");
    }

    @Test(description = "Kiểm tra đăng nhập trực tiếp qua đối tượng TestAccount")
    public void testLoginWithAccountObject() {
        LoginPage loginPage = openLoginPage();

        // TV4/TV5 có thể truyền trực tiếp Enum TestAccount.ADMIN
        loginPage.loginAs(TestAccount.ADMIN);

        // Chờ chuyển hướng hoàn tất sau khi đăng nhập thành công
        new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("/auth/login")));

        Assert.assertTrue(getDriver().getCurrentUrl().contains("admin")
                || getDriver().getCurrentUrl().contains("dashboard")
                || !getDriver().getCurrentUrl().contains("/auth/login"),
                "Admin đăng nhập thành công vào hệ thống");
    }
}
