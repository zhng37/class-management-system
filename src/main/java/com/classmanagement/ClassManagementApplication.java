package com.classmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Spring Boot 主启动类
 */
@SpringBootApplication
@ServletComponentScan
public class ClassManagementApplication {
    public static void main(String[] args) {
        // 初始化数据库
        com.classmanagement.util.DatabaseInitializer.initializeDatabase();
        
        // 启动Spring Boot应用
        SpringApplication.run(ClassManagementApplication.class, args);
        
        System.out.println("\n========================================");
        System.out.println("  班级事务管理系统已启动！");
        System.out.println("  访问地址: http://localhost:8081");
        System.out.println("========================================\n");
    }
}


