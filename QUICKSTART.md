# 快速启动指南

## 前置条件

1. **安装JDK 1.8或更高版本**
   - 检查安装：`java -version`
   - 下载地址：https://www.oracle.com/java/technologies/downloads/

2. **安装Maven 3.6+**
   - 检查安装：`mvn -version`
   - 下载地址：https://maven.apache.org/download.cgi

3. **安装MySQL 8.0+**
   - 确保MySQL服务正在运行
   - 下载地址：https://dev.mysql.com/downloads/mysql/

## 快速开始（5分钟）

### 步骤1：创建数据库

打开MySQL命令行或客户端，执行：

```sql
CREATE DATABASE class_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

或者直接执行项目中的SQL脚本：

```bash
mysql -u root -p < database/init.sql
```

### 步骤2：配置数据库连接

编辑 `src/main/java/com/classmanagement/config/DatabaseConfig.java`，修改数据库连接信息：

```java
config.setJdbcUrl("jdbc:mysql://localhost:3306/class_management?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8");
config.setUsername("root");  // 改为你的MySQL用户名
config.setPassword("your_password");  // 改为你的MySQL密码
```

### 步骤3：编译项目

在项目根目录执行：

```bash
mvn clean compile
```

### 步骤4：运行项目

方式1：使用Maven运行
```bash
mvn exec:java -Dexec.mainClass="com.classmanagement.Main"
```

方式2：在IDE中运行
- 打开项目（IntelliJ IDEA 或 Eclipse）
- 找到 `src/main/java/com/classmanagement/Main.java`
- 右键选择"Run"或"运行"

### 步骤5：首次使用

1. 程序启动后，选择 **"2. 注册"** 创建第一个账号
2. 建议创建一个**老师**或**班委**账号，以便管理其他用户
3. 注册成功后，选择 **"1. 登录"** 登录系统
4. 登录后即可使用各项功能

## 测试账号

如果使用 `database/init.sql` 脚本初始化数据库，会创建一个默认管理员账号：
- 用户名：`admin`
- 密码：`admin123`
- 角色：老师

## 常见问题

### 1. 数据库连接失败

**错误信息**：`Communications link failure` 或 `Access denied`

**解决方法**：
- 检查MySQL服务是否启动
- 检查数据库用户名和密码是否正确
- 检查数据库是否已创建
- 检查防火墙设置

### 2. 找不到主类

**错误信息**：`Could not find or load main class`

**解决方法**：
- 确保已执行 `mvn clean compile`
- 检查IDE是否正确识别Maven项目
- 重新导入Maven项目

### 3. 字符编码问题

**解决方法**：
- 确保数据库使用 `utf8mb4` 字符集
- 确保Java文件使用UTF-8编码
- 检查IDE的文件编码设置

### 4. 依赖下载失败

**解决方法**：
- 检查网络连接
- 配置Maven镜像源（如阿里云镜像）
- 清理Maven缓存：`mvn clean`

## 下一步

- 查看 [README.md](README.md) 了解详细功能说明
- 查看源代码了解实现细节
- 根据需要修改和扩展功能

## 获取帮助

如遇到问题，请检查：
1. 所有前置条件是否满足
2. 数据库配置是否正确
3. 日志中的错误信息

祝使用愉快！


