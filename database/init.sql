-- 班级事务管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS class_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE class_management;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    role ENUM('STUDENT', 'COMMITTEE', 'TEACHER') NOT NULL DEFAULT 'STUDENT',
    student_id VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建公告表
CREATE TABLE IF NOT EXISTS announcements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    publisher_id INT NOT NULL,
    publish_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_important BOOLEAN DEFAULT FALSE,
    status ENUM('DRAFT', 'PUBLISHED') DEFAULT 'PUBLISHED',
    FOREIGN KEY (publisher_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建班费表
CREATE TABLE IF NOT EXISTS class_fees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    description VARCHAR(500),
    operator_id INT NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (operator_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建活动表
CREATE TABLE IF NOT EXISTS activities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(200),
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    organizer_id INT NOT NULL,
    max_participants INT,
    status ENUM('REGISTERING', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'REGISTERING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建活动报名表
CREATE TABLE IF NOT EXISTS activity_registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    activity_id INT NOT NULL,
    user_id INT NOT NULL,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_checked_in BOOLEAN DEFAULT FALSE,
    check_in_time TIMESTAMP NULL,
    UNIQUE KEY unique_registration (activity_id, user_id),
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建考勤表
CREATE TABLE IF NOT EXISTS attendances (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT', 'LATE', 'LEAVE') NOT NULL,
    remark VARCHAR(500),
    recorder_id INT NOT NULL,
    record_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_attendance (user_id, attendance_date),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recorder_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建文件表
CREATE TABLE IF NOT EXISTS files (
    id INT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(100),
    uploader_id INT NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(500),
    download_count INT DEFAULT 0,
    FOREIGN KEY (uploader_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入示例数据（可选）
-- 插入一个管理员账号（用户名：admin，密码：admin123）
INSERT INTO users (username, password, real_name, role) 
VALUES ('admin', 'admin123', '系统管理员', 'TEACHER')
ON DUPLICATE KEY UPDATE username=username;


