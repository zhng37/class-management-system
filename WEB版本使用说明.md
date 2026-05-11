# Web版本使用说明

## 🚀 快速启动

### 1. 配置数据库

编辑 `src/main/java/com/classmanagement/config/DatabaseConfig.java`，修改数据库连接信息：

```java
config.setUsername("你的MySQL用户名");
config.setPassword("你的MySQL密码");
```

### 2. 创建数据库

在MySQL中执行：
```sql
CREATE DATABASE class_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 启动应用

**方式1：使用Maven命令**
```bash
mvn spring-boot:run
```

**方式2：在IDE中运行**
- 找到 `ClassManagementApplication.java`
- 右键 → Run

### 4. 访问系统

启动成功后，在浏览器访问：
```
http://localhost:8080
```

## 📱 功能使用

### 登录/注册

1. 首次使用请先注册账号
2. 建议创建**老师**或**班委**角色，以便管理其他用户
3. 注册成功后使用账号密码登录

### 主要功能

#### 公告管理
- 查看所有公告
- 班委/老师可以发布、删除公告
- 支持重要公告标记

#### 班费管理
- 查看班费明细和余额
- 班委/老师可以记录收入和支出

#### 活动管理
- 查看所有活动
- 报名/取消报名活动
- 班委/老师可以创建活动、查看报名列表、进行签到

#### 考勤管理
- 学生可以查看自己的考勤记录
- 班委/老师可以记录考勤、按日期查看考勤

#### 文件共享
- 上传和下载文件
- 查看文件列表和下载次数
- 班委/老师可以删除文件

#### 用户管理（仅班委/老师）
- 查看所有用户
- 删除用户

## 🎨 界面特点

- 现代化UI设计
- 响应式布局，支持移动端
- 清晰的导航和功能分类
- 友好的用户交互体验

## ⚙️ 配置说明

### 修改端口

编辑 `src/main/resources/application.properties`：
```properties
server.port=8080  # 改为你想要的端口
```

### 文件上传限制

在 `application.properties` 中配置：
```properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

## 🔧 常见问题

### 1. 无法访问页面

- 检查应用是否正常启动
- 确认端口号是否正确（默认8080）
- 检查防火墙设置

### 2. 数据库连接失败

- 确认MySQL服务已启动
- 检查数据库配置是否正确
- 确认数据库已创建

### 3. 文件上传失败

- 检查上传目录权限
- 检查文件大小是否超过限制

## 📝 注意事项

1. **会话管理**：系统使用Session管理用户登录状态，关闭浏览器后需要重新登录
2. **权限控制**：不同角色有不同的功能权限，学生只能查看，班委和老师可以管理
3. **文件存储**：上传的文件存储在项目根目录的 `uploads/` 文件夹中

## 🎯 下一步

- 可以继续使用控制台版本（运行 `Main.java`）
- 或者使用Web版本（运行 `ClassManagementApplication.java`）
- 两个版本共享同一套数据库和业务逻辑

享受使用！🎉


