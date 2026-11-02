package com.learnhub.automation.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Tiện ích dọn dẹp và reset dữ liệu kiểm thử trực tiếp qua JDBC PostgreSQL.
 * Giúp các kịch bản kiểm thử có tính Idempotent (chạy lặp lại nhiều lần mà không bị lỗi dữ liệu trùng lặp).
 */
public class DbResetUtil {
    private static final Logger LOGGER = Logger.getLogger(DbResetUtil.class.getName());

    private static String dbUrl = "jdbc:postgresql://localhost:5432/learnhub_db?stringtype=unspecified";
    private static String dbUser = "postgres";
    private static String dbPass = "postgres";
    private static String dbDriver = "org.postgresql.Driver";

    static {
        try (InputStream is = DbResetUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                dbUrl = props.getProperty("db.url", dbUrl);
                dbUser = props.getProperty("db.username", dbUser);
                dbPass = props.getProperty("db.password", dbPass);
                dbDriver = props.getProperty("db.driver", dbDriver);
            }
            Class.forName(dbDriver);
        } catch (Exception e) {
            LOGGER.warning("Không thể load db.properties, sử dụng cấu hình mặc định: " + e.getMessage());
        }
    }

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    /**
     * Dọn dẹp dữ liệu thử nghiệm phát sinh (các tài khoản, đăng ký, bài nộp do automation test tạo ra).
     */
    public static void cleanTestGeneratedData() {
        String cleanSql = """
            -- Xóa các kết quả làm bài quiz do test sinh ra
            DELETE FROM quiz_submission 
            WHERE user_id IN (SELECT id FROM "user" WHERE email LIKE '%@test.com' OR username LIKE 'test%');
            
            -- Xóa các lượt đăng ký khóa học do test sinh ra
            DELETE FROM registration 
            WHERE user_id IN (SELECT id FROM "user" WHERE email LIKE '%@test.com' OR username LIKE 'test%');
            
            -- Xóa người dùng thử nghiệm được tạo trong các test case đăng ký tài khoản
            DELETE FROM "user" 
            WHERE email LIKE '%@test.com' OR username LIKE 'testuser%' OR email LIKE 'test_%@%';
        """;

        executeSql(cleanSql, "Dọn dẹp dữ liệu rác của Automation Test");
    }

    /**
     * Thực thi câu lệnh SQL tùy ý phục vụ việc chuẩn bị hoặc dọn dẹp dữ liệu.
     */
    public static void executeSql(String sql, String actionDescription) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            LOGGER.info("[DB Cleanup] " + actionDescription + " thành công!");
        } catch (Exception e) {
            LOGGER.warning("[DB Cleanup] " + actionDescription + " gặp lỗi (hoặc DB chưa kết nối): " + e.getMessage());
        }
    }
}

