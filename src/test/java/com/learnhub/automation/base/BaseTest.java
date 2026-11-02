package com.learnhub.automation.base;

import com.learnhub.automation.pages.LoginPage;
import com.learnhub.automation.utils.DbResetUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

/**
 * Lớp kiểm thử cơ sở (Base Test) do Tech Lead (TV3) xây dựng.
 * Quản lý toàn bộ vòng đời của WebDriver, thiết lập timeouts, và điều hướng cơ bản.
 * TV4 và TV5 chỉ cần kế thừa lớp này (extends BaseTest) để viết các test script.
 */
public abstract class BaseTest {
    // ThreadLocal giúp hỗ trợ chạy song song đa luồng an toàn mà không xung đột trình duyệt
    protected static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Cấu hình URL mặc định, hỗ trợ ghi đè linh hoạt qua cờ -DbaseUrl của Maven
    protected static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080/learnhub");
    protected static final String LOGIN_URL = BASE_URL + "/auth/login";

    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("==================================================");
        System.out.println(">>> [Test Suite Setup] Dọn dẹp cơ sở dữ liệu trước kiểm thử...");
        System.out.println("==================================================");
        DbResetUtil.cleanTestGeneratedData();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        // Hỗ trợ chế độ chạy ngầm (Headless) khi cần chạy trên CI/CD: mvn test -Dheadless=true
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        // Selenium 4.6+ tự động quản lý ChromeDriver thông qua Selenium Manager
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driver.set(webDriver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } finally {
                driver.remove();
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("==================================================");
        System.out.println(">>> [Test Suite Teardown] Dọn dẹp cơ sở dữ liệu sau kiểm thử...");
        System.out.println("==================================================");
        DbResetUtil.cleanTestGeneratedData();
    }

    /**
     * Mở trực tiếp trang đăng nhập và trả về đối tượng LoginPage.
     */
    protected LoginPage openLoginPage() {
        getDriver().get(LOGIN_URL);
        return new LoginPage(getDriver());
    }

    /**
     * Điều hướng đến đường dẫn tương đối trong hệ thống (ví dụ: "/courses", "/home").
     */
    protected void navigateTo(String relativePath) {
        String url = relativePath.startsWith("http") ? relativePath : BASE_URL + relativePath;
        getDriver().get(url);
    }
}

