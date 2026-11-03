package com.learnhub.automation.tests;

import com.learnhub.automation.base.BaseTest;
import com.learnhub.automation.model.TestAccount;
import com.learnhub.automation.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/** Ba test độc lập để demo đăng nhập; mỗi test dùng một trình duyệt mới. */
public class LoginDemoTest extends BaseTest {

    @Test(priority = 1, description = "TC01 - Đăng nhập thành công bằng email hợp lệ")
    public void TC01_loginWithValidEmail() {
        LoginPage loginPage = openLoginPage();

        loginPage.loginAs(TestAccount.STUDENT);

        assertStudentDashboard();
    }

    @Test(priority = 2, description = "TC02 - Đăng nhập thành công bằng username hợp lệ")
    public void TC02_loginWithValidUsername() {
        LoginPage loginPage = openLoginPage();

        // Ô Email or Username nhận cả hai loại thông tin đăng nhập.
        loginPage.login("student", TestAccount.STUDENT.getPassword());

        assertStudentDashboard();
    }

    private void assertStudentDashboard() {
        String expectedUrl = BASE_URL + "/my-enrollments";
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        // Đợi đúng trang đích, tránh kết luận thành công chỉ vì đã rời trang login.
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        Assert.assertEquals(getDriver().getCurrentUrl(), expectedUrl,
                "Student phải được chuyển đến trang khóa học đã đăng ký");

        String heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")))
                .getText().trim();
        Assert.assertEquals(heading, "My Courses",
                "Trang đích phải hiển thị nội dung My Courses");
        Assert.assertFalse(getDriver().findElements(By.cssSelector("a[href$='/auth/logout']")).isEmpty(),
                "Phải có liên kết Sign out dành cho người dùng đã đăng nhập");
    }
}
