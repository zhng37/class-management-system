# 班级事务管理系统

一个基于Java开发的班级事务管理系统，提供用户管理、公告发布、班费管理、活动管理、考勤管理和文件共享等功能。

## 功能特性

### 1. 用户管理
- 支持三种角色：学生、班委、老师
- 用户注册和登录
- 用户信息管理（仅班委和老师可管理）

### 2. 班级公告
- 发布和查看公告
- 重要公告标记
- 公告推送功能

### 3. 班费管理
- 班费收入记录
- 班费支出记录
- 班费明细查询
- 班费余额统计

### 4. 活动管理
- 创建活动
- 活动报名
- 活动签到
- 报名列表查看

### 5. 考勤管理
- 记录考勤（出勤、缺勤、迟到、请假）
- 考勤记录查询
- 考勤统计

### 6. 文件共享
- 文件上传
- 文件下载
- 文件列表查看
- 下载次数统计

## 技术栈

- **开发语言**: Java 8+
- **Web框架**: Spring Boot 2.7.14
- **模板引擎**: Thymeleaf
- **数据库**: MySQL 8.0+
- **连接池**: HikariCP
- **构建工具**: Maven
- **JSON处理**: Gson

## 环境要求

- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- IDE（推荐 IntelliJ IDEA 或 Eclipse）

## 数据库配置

1. 创建MySQL数据库：
```sql
CREATE DATABASE class_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改数据库连接配置：
编辑 `src/main/java/com/classmanagement/config/DatabaseConfig.java`，修改以下配置：
```java
config.setJdbcUrl("jdbc:mysql://localhost:3306/class_management?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8");
config.setUsername("root");  // 修改为你的MySQL用户名
config.setPassword("root");  // 修改为你的MySQL密码
```

## 安装和运行

### 1. 克隆或下载项目

### 2. 配置数据库
- 确保MySQL服务已启动
- 创建数据库（如上述SQL）
- 修改数据库连接配置

### 3. 编译项目
```bash
mvn clean compile
```

### 4. 运行项目

**Web版本（推荐）**：
```bash
mvn spring-boot:run
```

或者在IDE中运行 `ClassManagementApplication.java`

**控制台版本（旧版）**：
```bash
mvn exec:java -Dexec.mainClass="com.classmanagement.Main"
```

### 5. 访问系统

启动成功后，在浏览器中访问：
```
http://localhost:8080
```

### 6. 初始化数据库
程序首次运行时会自动创建所有必要的数据库表。

## 使用说明

### 首次使用（Web版本）

1. 启动程序后，在浏览器访问 `http://localhost:8080`
2. 点击"还没有账号？立即注册"创建第一个用户
3. 建议先创建一个**老师**或**班委**账号，以便管理其他用户
4. 注册成功后，使用账号登录
5. 登录后即可使用各项功能

### 首次使用（控制台版本）

1. 启动程序后，选择"注册"创建第一个用户
2. 建议先创建一个老师或班委账号，以便管理其他用户
3. 登录后即可使用各项功能

### 角色权限

- **学生**: 可以查看公告、查看班费明细、报名活动、查看考勤、上传/下载文件
- **班委**: 拥有学生所有权限，还可以发布公告、管理班费、创建活动、记录考勤、管理用户
- **老师**: 拥有所有权限，包括用户管理

### 主要功能操作

#### 公告管理
- 班委和老师可以发布、修改、删除公告
- 所有用户都可以查看公告

#### 班费管理
- 班委和老师可以记录收入和支出
- 所有用户都可以查看班费明细和余额

#### 活动管理
- 班委和老师可以创建活动
- 所有用户都可以报名活动
- 班委和老师可以进行签到操作

#### 考勤管理
- 班委和老师可以记录考勤
- 所有用户都可以查看自己的考勤记录
- 支持按日期查询和统计

#### 文件共享
- 所有用户都可以上传和下载文件
- 班委和老师可以删除文件

## 项目结构

```
class-management-system/
├── pom.xml                          # Maven配置文件
├── README.md                        # 项目说明文档
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── classmanagement/
        │           ├── ClassManagementApplication.java  # Spring Boot主启动类
        │           ├── Main.java                       # 控制台版本主程序（可选）
        │           ├── config/
        │           │   ├── DatabaseConfig.java         # 数据库配置
        │           │   ├── WebConfig.java              # Web配置
        │           │   └── SessionConfig.java          # 会话配置
        │           ├── controller/                     # 控制器层（Web版本）
        │           │   ├── IndexController.java
        │           │   ├── UserController.java
        │           │   ├── AnnouncementController.java
        │           │   ├── ClassFeeController.java
        │           │   ├── ActivityController.java
        │           │   ├── AttendanceController.java
        │           │   └── FileController.java
        │           ├── dao/                             # 数据访问层
        │           ├── entity/                         # 实体类
        │           ├── service/                        # 业务逻辑层
        │           └── util/
        │               └── DatabaseInitializer.java
        └── resources/
            ├── application.properties                  # Spring Boot配置
            ├── templates/                              # Thymeleaf模板
            │   ├── login.html
            │   ├── register.html
            │   ├── home.html
            │   ├── fragments/                         # 页面片段
            │   ├── announcement/
            │   ├── fee/
            │   ├── activity/
            │   ├── attendance/
            │   ├── file/
            │   └── user/
            └── static/                                 # 静态资源
                └── css/
                    └── style.css
```

## 数据库表结构

- **users**: 用户表
- **announcements**: 公告表
- **class_fees**: 班费表
- **activities**: 活动表
- **activity_registrations**: 活动报名表
- **attendances**: 考勤表
- **files**: 文件表

详细表结构请查看 `DatabaseInitializer.java` 中的SQL语句。

## 注意事项

1. **密码安全**: 当前版本密码以明文存储，生产环境建议使用加密存储
2. **文件存储**: 文件路径存储在数据库中，实际文件需要手动管理
3. **数据库连接**: 确保MySQL服务正常运行，且数据库已创建
4. **时区设置**: 数据库连接URL中已设置时区为Asia/Shanghai

## 后续改进建议

1. 添加密码加密功能（如使用BCrypt）
2. 实现文件实际存储和下载功能
3. 添加Web界面（使用Spring Boot + Thymeleaf或Vue.js）
4. 添加权限验证框架（如Spring Security）
5. 添加日志记录功能
6. 添加数据备份和恢复功能
7. 优化用户体验和界面

## 许可证

本项目仅供学习和教学使用。

## 联系方式

如有问题或建议，欢迎提出Issue。

