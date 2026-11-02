package com.learnhub.automation.model;

/**
 * Quản lý 4 tài khoản kiểm thử mặc định từ seed.sql cho TV4 và TV5.
 */
public enum TestAccount {
    ADMIN("Admin", "admin@learnhub.com", "admin"),
    MANAGER("Manager", "manager@learnhub.com", "manager"),
    EXPERT("Expert", "expert@learnhub.com", "expert"),
    STUDENT("Student", "student@learnhub.com", "student");

    private final String roleName;
    private final String email;
    private final String password;

    TestAccount(String roleName, String email, String password) {
        this.roleName = roleName;
        this.email = email;
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Tìm kiếm tài khoản theo tên vai trò (không phân biệt chữ hoa, chữ thường).
     * Ví dụ: "admin", "Admin", "ADMIN", "Manager",...
     */
    public static TestAccount fromRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Vai trò (role) không được để trống!");
        }
        for (TestAccount acc : values()) {
            if (acc.name().equalsIgnoreCase(role.trim()) || acc.roleName.equalsIgnoreCase(role.trim())) {
                return acc;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy cấu hình tài khoản cho vai trò: " + role);
    }
}
