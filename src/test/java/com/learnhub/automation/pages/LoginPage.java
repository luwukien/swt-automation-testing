package com.learnhub.automation.pages;

import com.learnhub.automation.model.TestAccount;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model cho trang Đăng nhập (/auth/login).
 * Cung cấp locators chuẩn xác theo giao diện login.jsp và các hành vi tương tác.
 */
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators định vị từ form giao diện login.jsp thực tế
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By errorMessageAlert = By.cssSelector(".bg-rose-50 span");
    private final By successMessageAlert = By.cssSelector(".bg-emerald-50 span");
    private final By forgotPasswordLink = By.cssSelector("a[href*='forgot-password']");
    private final By registerLink = By.cssSelector("a[href*='register']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage enterEmail(String email) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        input.clear();
        if (email != null) {
            input.sendKeys(email);
        }
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        input.clear();
        if (password != null) {
            input.sendKeys(password);
        }
        return this;
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    /**
     * Hàm đăng nhập cốt lõi theo vai trò (Admin, Manager, Expert, Student)
     * Dùng chung cho TV4 và TV5.
     *
     * @param role Tên vai trò, ví dụ: "Admin", "Manager", "Expert", "Student"
     */
    public void loginAs(String role) {
        TestAccount account = TestAccount.fromRole(role);
        loginAs(account);
    }

    /**
     * Đăng nhập với đối tượng TestAccount cụ thể.
     */
    public void loginAs(TestAccount account) {
        enterEmail(account.getEmail());
        enterPassword(account.getPassword());
        clickSubmit();
    }

    /**
     * Đăng nhập với email và password tùy biến (dùng cho test case đăng nhập sai, để trống,...).
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    /**
     * Lấy thông báo lỗi hiển thị trên trang đăng nhập (nếu có).
     */
    public String getErrorMessage() {
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageAlert));
        return alert.getText().trim();
    }

    /**
     * Lấy thông báo thành công hiển thị trên trang (ví dụ sau khi reset mật khẩu thành công).
     */
    public String getSuccessMessage() {
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageAlert));
        return alert.getText().trim();
    }

    /**
     * Kiểm tra nút Submit có đang hiển thị hay không.
     */
    public boolean isSubmitButtonDisplayed() {
        try {
            return driver.findElement(submitButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

