package com.classmanagement;

import com.classmanagement.dao.AttendanceDAO;
import com.classmanagement.entity.User;
import com.classmanagement.service.*;
import com.classmanagement.util.DatabaseInitializer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

/**
 * 班级事务管理系统主程序
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static UserService userService = new UserService();
    private static AnnouncementService announcementService = new AnnouncementService();
    private static ClassFeeService classFeeService = new ClassFeeService();
    private static ActivityService activityService = new ActivityService();
    private static AttendanceService attendanceService = new AttendanceService();
    private static FileService fileService = new FileService();
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     欢迎使用班级事务管理系统");
        System.out.println("========================================");
        
        // 初始化数据库
        System.out.println("\n正在初始化数据库...");
        DatabaseInitializer.initializeDatabase();
        System.out.println("数据库初始化完成！\n");
        
        // 主菜单循环
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    /**
     * 显示登录菜单
     */
    private static void showLoginMenu() {
        System.out.println("\n========== 登录菜单 ==========");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("0. 退出系统");
        System.out.print("请选择操作: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 0:
                System.out.println("感谢使用，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }
    
    /**
     * 显示主菜单
     */
    private static void showMainMenu() {
        System.out.println("\n========== 主菜单 ==========");
        System.out.println("当前用户: " + currentUser.getRealName() + " (" + currentUser.getRole().getDescription() + ")");
        System.out.println("1. 公告管理");
        System.out.println("2. 班费管理");
        System.out.println("3. 活动管理");
        System.out.println("4. 考勤管理");
        System.out.println("5. 文件共享");
        if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
            System.out.println("6. 用户管理");
        }
        System.out.println("0. 退出登录");
        System.out.print("请选择操作: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                announcementMenu();
                break;
            case 2:
                classFeeMenu();
                break;
            case 3:
                activityMenu();
                break;
            case 4:
                attendanceMenu();
                break;
            case 5:
                fileMenu();
                break;
            case 6:
                if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                    userManagementMenu();
                } else {
                    System.out.println("权限不足！");
                }
                break;
            case 0:
                currentUser = null;
                System.out.println("已退出登录");
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }
    
    /**
     * 登录
     */
    private static void login() {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        
        currentUser = userService.login(username, password);
        if (currentUser != null) {
            System.out.println("登录成功！欢迎，" + currentUser.getRealName() + "！");
        } else {
            System.out.println("登录失败！用户名或密码错误！");
        }
    }
    
    /**
     * 注册
     */
    private static void register() {
        System.out.println("\n========== 用户注册 ==========");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        System.out.print("请输入真实姓名: ");
        String realName = scanner.nextLine();
        System.out.print("请选择角色 (1-学生, 2-班委, 3-老师): ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        
        User.Role role = User.Role.STUDENT;
        switch (roleChoice) {
            case 1:
                role = User.Role.STUDENT;
                break;
            case 2:
                role = User.Role.COMMITTEE;
                break;
            case 3:
                role = User.Role.TEACHER;
                break;
        }
        
        System.out.print("请输入学号（可选）: ");
        String studentId = scanner.nextLine();
        System.out.print("请输入手机号（可选）: ");
        String phone = scanner.nextLine();
        System.out.print("请输入邮箱（可选）: ");
        String email = scanner.nextLine();
        
        User user = new User(username, password, realName, role);
        user.setStudentId(studentId.isEmpty() ? null : studentId);
        user.setPhone(phone.isEmpty() ? null : phone);
        user.setEmail(email.isEmpty() ? null : email);
        
        if (userService.register(user)) {
            System.out.println("注册成功！");
        } else {
            System.out.println("注册失败！用户名可能已存在。");
        }
    }
    
    /**
     * 公告管理菜单
     */
    private static void announcementMenu() {
        while (true) {
            System.out.println("\n========== 公告管理 ==========");
            System.out.println("1. 查看所有公告");
            System.out.println("2. 查看公告详情");
            if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                System.out.println("3. 发布公告");
                System.out.println("4. 修改公告");
                System.out.println("5. 删除公告");
            }
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewAllAnnouncements();
                    break;
                case 2:
                    viewAnnouncementDetail();
                    break;
                case 3:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        publishAnnouncement();
                    }
                    break;
                case 4:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        updateAnnouncement();
                    }
                    break;
                case 5:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        deleteAnnouncement();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }
    
    private static void viewAllAnnouncements() {
        List<com.classmanagement.entity.Announcement> announcements = announcementService.getAllPublishedAnnouncements();
        if (announcements.isEmpty()) {
            System.out.println("暂无公告");
            return;
        }
        System.out.println("\n公告列表:");
        for (com.classmanagement.entity.Announcement a : announcements) {
            System.out.printf("[%s] %s - %s (%s)\n",
                    a.getIsImportant() ? "重要" : "普通",
                    a.getTitle(),
                    a.getPublisherName() != null ? a.getPublisherName() : "未知",
                    a.getPublishTime());
        }
    }
    
    private static void viewAnnouncementDetail() {
        System.out.print("请输入公告ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        com.classmanagement.entity.Announcement a = announcementService.getAnnouncementById(id);
        if (a != null) {
            System.out.println("\n标题: " + a.getTitle());
            System.out.println("内容: " + a.getContent());
            System.out.println("发布者: " + (a.getPublisherName() != null ? a.getPublisherName() : "未知"));
            System.out.println("发布时间: " + a.getPublishTime());
            System.out.println("重要性: " + (a.getIsImportant() ? "重要" : "普通"));
        } else {
            System.out.println("公告不存在！");
        }
    }
    
    private static void publishAnnouncement() {
        System.out.print("请输入公告标题: ");
        String title = scanner.nextLine();
        System.out.print("请输入公告内容: ");
        String content = scanner.nextLine();
        System.out.print("是否重要 (y/n): ");
        boolean isImportant = scanner.nextLine().equalsIgnoreCase("y");
        
        com.classmanagement.entity.Announcement announcement = new com.classmanagement.entity.Announcement(
                title, content, currentUser.getId());
        announcement.setIsImportant(isImportant);
        
        if (announcementService.publishAnnouncement(announcement)) {
            System.out.println("公告发布成功！");
        } else {
            System.out.println("公告发布失败！");
        }
    }
    
    private static void updateAnnouncement() {
        System.out.print("请输入要修改的公告ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        com.classmanagement.entity.Announcement a = announcementService.getAnnouncementById(id);
        if (a == null) {
            System.out.println("公告不存在！");
            return;
        }
        
        System.out.print("请输入新标题（直接回车保持原值）: ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            a.setTitle(title);
        }
        
        System.out.print("请输入新内容（直接回车保持原值）: ");
        String content = scanner.nextLine();
        if (!content.isEmpty()) {
            a.setContent(content);
        }
        
        if (announcementService.updateAnnouncement(a)) {
            System.out.println("公告修改成功！");
        } else {
            System.out.println("公告修改失败！");
        }
    }
    
    private static void deleteAnnouncement() {
        System.out.print("请输入要删除的公告ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (announcementService.deleteAnnouncement(id)) {
            System.out.println("公告删除成功！");
        } else {
            System.out.println("公告删除失败！");
        }
    }
    
    /**
     * 班费管理菜单
     */
    private static void classFeeMenu() {
        while (true) {
            System.out.println("\n========== 班费管理 ==========");
            System.out.println("1. 查看班费明细");
            System.out.println("2. 查看班费余额");
            if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                System.out.println("3. 记录班费收入");
                System.out.println("4. 记录班费支出");
            }
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewClassFeeDetails();
                    break;
                case 2:
                    viewBalance();
                    break;
                case 3:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        recordIncome();
                    }
                    break;
                case 4:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        recordExpense();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }
    
    private static void viewClassFeeDetails() {
        List<com.classmanagement.entity.ClassFee> fees = classFeeService.getAllClassFees();
        if (fees.isEmpty()) {
            System.out.println("暂无班费记录");
            return;
        }
        System.out.println("\n班费明细:");
        System.out.printf("%-10s %-10s %-10s %-15s %-20s %-20s\n",
                "类型", "金额", "用户", "操作员", "说明", "时间");
        for (com.classmanagement.entity.ClassFee fee : fees) {
            System.out.printf("%-10s %-10s %-10s %-15s %-20s %-20s\n",
                    fee.getType().getDescription(),
                    fee.getAmount(),
                    fee.getUserName() != null ? fee.getUserName() : "未知",
                    fee.getOperatorName() != null ? fee.getOperatorName() : "未知",
                    fee.getDescription() != null ? fee.getDescription() : "",
                    fee.getTransactionTime());
        }
    }
    
    private static void viewBalance() {
        BigDecimal balance = classFeeService.getBalance();
        System.out.println("\n当前班费余额: " + balance + " 元");
    }
    
    private static void recordIncome() {
        System.out.print("请输入缴费用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入金额: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("请输入说明: ");
        String description = scanner.nextLine();
        
        com.classmanagement.entity.ClassFee fee = new com.classmanagement.entity.ClassFee(
                userId, amount, com.classmanagement.entity.ClassFee.Type.INCOME, currentUser.getId());
        fee.setDescription(description);
        
        if (classFeeService.addClassFee(fee)) {
            System.out.println("收入记录成功！");
        } else {
            System.out.println("收入记录失败！");
        }
    }
    
    private static void recordExpense() {
        System.out.print("请输入支出用户ID（可为0表示班级支出）: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入金额: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("请输入说明: ");
        String description = scanner.nextLine();
        
        com.classmanagement.entity.ClassFee fee = new com.classmanagement.entity.ClassFee(
                userId, amount, com.classmanagement.entity.ClassFee.Type.EXPENSE, currentUser.getId());
        fee.setDescription(description);
        
        if (classFeeService.addClassFee(fee)) {
            System.out.println("支出记录成功！");
        } else {
            System.out.println("支出记录失败！");
        }
    }
    
    /**
     * 活动管理菜单
     */
    private static void activityMenu() {
        while (true) {
            System.out.println("\n========== 活动管理 ==========");
            System.out.println("1. 查看所有活动");
            System.out.println("2. 查看活动详情");
            System.out.println("3. 报名活动");
            System.out.println("4. 查看我的报名");
            System.out.println("5. 取消报名");
            if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                System.out.println("6. 创建活动");
                System.out.println("7. 活动签到");
                System.out.println("8. 查看活动报名列表");
            }
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewAllActivities();
                    break;
                case 2:
                    viewActivityDetail();
                    break;
                case 3:
                    registerActivity();
                    break;
                case 4:
                    viewMyRegistrations();
                    break;
                case 5:
                    cancelRegistration();
                    break;
                case 6:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        createActivity();
                    }
                    break;
                case 7:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        checkInActivity();
                    }
                    break;
                case 8:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        viewActivityRegistrations();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }
    
    private static void viewAllActivities() {
        List<com.classmanagement.entity.Activity> activities = activityService.getAllActivities();
        if (activities.isEmpty()) {
            System.out.println("暂无活动");
            return;
        }
        System.out.println("\n活动列表:");
        for (com.classmanagement.entity.Activity a : activities) {
            System.out.printf("[%s] %s - %s (%s)\n",
                    a.getStatus().getDescription(),
                    a.getTitle(),
                    a.getOrganizerName() != null ? a.getOrganizerName() : "未知",
                    a.getStartTime());
        }
    }
    
    private static void viewActivityDetail() {
        System.out.print("请输入活动ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        com.classmanagement.entity.Activity a = activityService.getActivityById(id);
        if (a != null) {
            System.out.println("\n标题: " + a.getTitle());
            System.out.println("描述: " + a.getDescription());
            System.out.println("地点: " + (a.getLocation() != null ? a.getLocation() : "未设置"));
            System.out.println("开始时间: " + a.getStartTime());
            System.out.println("结束时间: " + (a.getEndTime() != null ? a.getEndTime() : "未设置"));
            System.out.println("组织者: " + (a.getOrganizerName() != null ? a.getOrganizerName() : "未知"));
            System.out.println("状态: " + a.getStatus().getDescription());
            System.out.println("报名人数: " + (a.getCurrentParticipants() != null ? a.getCurrentParticipants() : 0) +
                    (a.getMaxParticipants() != null ? "/" + a.getMaxParticipants() : ""));
        } else {
            System.out.println("活动不存在！");
        }
    }
    
    private static void registerActivity() {
        System.out.print("请输入活动ID: ");
        int activityId = scanner.nextInt();
        scanner.nextLine();
        if (activityService.registerActivity(activityId, currentUser.getId())) {
            System.out.println("报名成功！");
        } else {
            System.out.println("报名失败！可能已报名或人数已满。");
        }
    }
    
    private static void viewMyRegistrations() {
        List<com.classmanagement.entity.ActivityRegistration> registrations = activityService.getUserRegistrations(currentUser.getId());
        if (registrations.isEmpty()) {
            System.out.println("暂无报名记录");
            return;
        }
        System.out.println("\n我的报名:");
        for (com.classmanagement.entity.ActivityRegistration r : registrations) {
            System.out.printf("%s - %s [%s]\n",
                    r.getActivityTitle() != null ? r.getActivityTitle() : "未知活动",
                    r.getRegistrationTime(),
                    r.getIsCheckedIn() ? "已签到" : "未签到");
        }
    }
    
    private static void cancelRegistration() {
        System.out.print("请输入活动ID: ");
        int activityId = scanner.nextInt();
        scanner.nextLine();
        if (activityService.cancelRegistration(activityId, currentUser.getId())) {
            System.out.println("取消报名成功！");
        } else {
            System.out.println("取消报名失败！");
        }
    }
    
    private static void createActivity() {
        System.out.print("请输入活动标题: ");
        String title = scanner.nextLine();
        System.out.print("请输入活动描述: ");
        String description = scanner.nextLine();
        System.out.print("请输入活动地点: ");
        String location = scanner.nextLine();
        System.out.print("请输入开始时间 (yyyy-MM-dd HH:mm): ");
        String startTimeStr = scanner.nextLine();
        System.out.print("请输入结束时间 (yyyy-MM-dd HH:mm，可选): ");
        String endTimeStr = scanner.nextLine();
        System.out.print("请输入最大报名人数（可选，直接回车表示无限制）: ");
        String maxParticipantsStr = scanner.nextLine();
        
        try {
            Timestamp startTime = Timestamp.valueOf(startTimeStr + ":00");
            Timestamp endTime = endTimeStr.isEmpty() ? null : Timestamp.valueOf(endTimeStr + ":00");
            Integer maxParticipants = maxParticipantsStr.isEmpty() ? null : Integer.parseInt(maxParticipantsStr);
            
            com.classmanagement.entity.Activity activity = new com.classmanagement.entity.Activity(
                    title, description, startTime, currentUser.getId());
            activity.setLocation(location);
            activity.setEndTime(endTime);
            activity.setMaxParticipants(maxParticipants);
            
            if (activityService.createActivity(activity)) {
                System.out.println("活动创建成功！");
            } else {
                System.out.println("活动创建失败！");
            }
        } catch (Exception e) {
            System.out.println("时间格式错误！请使用 yyyy-MM-dd HH:mm 格式");
        }
    }
    
    private static void checkInActivity() {
        System.out.print("请输入活动ID: ");
        int activityId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        if (activityService.checkIn(activityId, userId)) {
            System.out.println("签到成功！");
        } else {
            System.out.println("签到失败！用户可能未报名。");
        }
    }
    
    private static void viewActivityRegistrations() {
        System.out.print("请输入活动ID: ");
        int activityId = scanner.nextInt();
        scanner.nextLine();
        List<com.classmanagement.entity.ActivityRegistration> registrations = activityService.getActivityRegistrations(activityId);
        if (registrations.isEmpty()) {
            System.out.println("暂无报名记录");
            return;
        }
        System.out.println("\n报名列表:");
        for (com.classmanagement.entity.ActivityRegistration r : registrations) {
            System.out.printf("%s - %s [%s]\n",
                    r.getUserName() != null ? r.getUserName() : "未知",
                    r.getRegistrationTime(),
                    r.getIsCheckedIn() ? "已签到" : "未签到");
        }
    }
    
    /**
     * 考勤管理菜单
     */
    private static void attendanceMenu() {
        while (true) {
            System.out.println("\n========== 考勤管理 ==========");
            System.out.println("1. 查看我的考勤记录");
            if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                System.out.println("2. 记录考勤");
                System.out.println("3. 查看指定日期考勤");
                System.out.println("4. 查看考勤统计");
            }
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewMyAttendance();
                    break;
                case 2:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        recordAttendance();
                    }
                    break;
                case 3:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        viewAttendanceByDate();
                    }
                    break;
                case 4:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        viewAttendanceStatistics();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }
    
    private static void viewMyAttendance() {
        List<com.classmanagement.entity.Attendance> attendances = attendanceService.getUserAttendance(currentUser.getId());
        if (attendances.isEmpty()) {
            System.out.println("暂无考勤记录");
            return;
        }
        System.out.println("\n我的考勤记录:");
        for (com.classmanagement.entity.Attendance a : attendances) {
            System.out.printf("%s - %s [%s]\n",
                    a.getAttendanceDate(),
                    a.getStatus().getDescription(),
                    a.getRemark() != null ? a.getRemark() : "");
        }
    }
    
    private static void recordAttendance() {
        System.out.print("请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入日期 (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        System.out.print("请选择状态 (1-出勤, 2-缺勤, 3-迟到, 4-请假): ");
        int statusChoice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入备注（可选）: ");
        String remark = scanner.nextLine();
        
        com.classmanagement.entity.Attendance.Status status = com.classmanagement.entity.Attendance.Status.PRESENT;
        switch (statusChoice) {
            case 1:
                status = com.classmanagement.entity.Attendance.Status.PRESENT;
                break;
            case 2:
                status = com.classmanagement.entity.Attendance.Status.ABSENT;
                break;
            case 3:
                status = com.classmanagement.entity.Attendance.Status.LATE;
                break;
            case 4:
                status = com.classmanagement.entity.Attendance.Status.LEAVE;
                break;
        }
        
        try {
            Date date = Date.valueOf(dateStr);
            com.classmanagement.entity.Attendance attendance = new com.classmanagement.entity.Attendance(
                    userId, date, status, currentUser.getId());
            attendance.setRemark(remark.isEmpty() ? null : remark);
            
            if (attendanceService.recordAttendance(attendance)) {
                System.out.println("考勤记录成功！");
            } else {
                System.out.println("考勤记录失败！");
            }
        } catch (Exception e) {
            System.out.println("日期格式错误！请使用 yyyy-MM-dd 格式");
        }
    }
    
    private static void viewAttendanceByDate() {
        System.out.print("请输入日期 (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        try {
            Date date = Date.valueOf(dateStr);
            List<com.classmanagement.entity.Attendance> attendances = attendanceService.getAttendanceByDate(date);
            if (attendances.isEmpty()) {
                System.out.println("该日期暂无考勤记录");
                return;
            }
            System.out.println("\n考勤记录:");
            for (com.classmanagement.entity.Attendance a : attendances) {
                System.out.printf("%s - %s [%s]\n",
                        a.getUserName() != null ? a.getUserName() : "未知",
                        a.getStatus().getDescription(),
                        a.getRemark() != null ? a.getRemark() : "");
            }
        } catch (Exception e) {
            System.out.println("日期格式错误！请使用 yyyy-MM-dd 格式");
        }
    }
    
    private static void viewAttendanceStatistics() {
        System.out.print("请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入开始日期 (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("请输入结束日期 (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        
        try {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            AttendanceDAO.AttendanceStatistics stats = attendanceService.getStatistics(userId, startDate, endDate);
            
            System.out.println("\n考勤统计:");
            System.out.println("出勤: " + stats.present);
            System.out.println("缺勤: " + stats.absent);
            System.out.println("迟到: " + stats.late);
            System.out.println("请假: " + stats.leave);
            System.out.println("总计: " + stats.getTotal());
        } catch (Exception e) {
            System.out.println("日期格式错误！请使用 yyyy-MM-dd 格式");
        }
    }
    
    /**
     * 文件管理菜单
     */
    private static void fileMenu() {
        while (true) {
            System.out.println("\n========== 文件共享 ==========");
            System.out.println("1. 查看所有文件");
            System.out.println("2. 查看文件详情");
            System.out.println("3. 上传文件");
            System.out.println("4. 查看我上传的文件");
            if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                System.out.println("5. 删除文件");
            }
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewAllFiles();
                    break;
                case 2:
                    viewFileDetail();
                    break;
                case 3:
                    uploadFile();
                    break;
                case 4:
                    viewMyFiles();
                    break;
                case 5:
                    if (currentUser.getRole() == User.Role.TEACHER || currentUser.getRole() == User.Role.COMMITTEE) {
                        deleteFile();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }
    
    private static void viewAllFiles() {
        List<com.classmanagement.entity.File> files = fileService.getAllFiles();
        if (files.isEmpty()) {
            System.out.println("暂无文件");
            return;
        }
        System.out.println("\n文件列表:");
        for (com.classmanagement.entity.File f : files) {
            System.out.printf("%s - %s (%s, 下载次数: %d)\n",
                    f.getOriginalName(),
                    f.getUploaderName() != null ? f.getUploaderName() : "未知",
                    f.getUploadTime(),
                    f.getDownloadCount());
        }
    }
    
    private static void viewFileDetail() {
        System.out.print("请输入文件ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        com.classmanagement.entity.File f = fileService.getFileById(id);
        if (f != null) {
            System.out.println("\n文件名: " + f.getOriginalName());
            System.out.println("文件大小: " + f.getFileSize() + " 字节");
            System.out.println("文件类型: " + (f.getFileType() != null ? f.getFileType() : "未知"));
            System.out.println("上传者: " + (f.getUploaderName() != null ? f.getUploaderName() : "未知"));
            System.out.println("上传时间: " + f.getUploadTime());
            System.out.println("下载次数: " + f.getDownloadCount());
            System.out.println("描述: " + (f.getDescription() != null ? f.getDescription() : "无"));
            System.out.println("文件路径: " + f.getFilePath());
            
            // 增加下载次数
            fileService.incrementDownloadCount(id);
        } else {
            System.out.println("文件不存在！");
        }
    }
    
    private static void uploadFile() {
        System.out.print("请输入文件路径: ");
        String filePath = scanner.nextLine();
        System.out.print("请输入原始文件名: ");
        String originalName = scanner.nextLine();
        System.out.print("请输入文件大小（字节）: ");
        long fileSize = scanner.nextLong();
        scanner.nextLine();
        System.out.print("请输入文件类型（可选）: ");
        String fileType = scanner.nextLine();
        System.out.print("请输入文件描述（可选）: ");
        String description = scanner.nextLine();
        
        // 生成文件名（可以使用UUID等）
        String fileName = System.currentTimeMillis() + "_" + originalName;
        
        com.classmanagement.entity.File file = new com.classmanagement.entity.File(
                fileName, originalName, filePath, fileSize, currentUser.getId());
        file.setFileType(fileType.isEmpty() ? null : fileType);
        file.setDescription(description.isEmpty() ? null : description);
        
        if (fileService.uploadFile(file)) {
            System.out.println("文件上传成功！");
        } else {
            System.out.println("文件上传失败！");
        }
    }
    
    private static void viewMyFiles() {
        List<com.classmanagement.entity.File> files = fileService.getUserFiles(currentUser.getId());
        if (files.isEmpty()) {
            System.out.println("暂无上传的文件");
            return;
        }
        System.out.println("\n我上传的文件:");
        for (com.classmanagement.entity.File f : files) {
            System.out.printf("%s - %s (下载次数: %d)\n",
                    f.getOriginalName(),
                    f.getUploadTime(),
                    f.getDownloadCount());
        }
    }
    
    private static void deleteFile() {
        System.out.print("请输入要删除的文件ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (fileService.deleteFile(id)) {
            System.out.println("文件删除成功！");
        } else {
            System.out.println("文件删除失败！");
        }
    }
    
    /**
     * 用户管理菜单
     */
    private static void userManagementMenu() {
        while (true) {
            System.out.println("\n========== 用户管理 ==========");
            System.out.println("1. 查看所有用户");
            System.out.println("2. 查看用户详情");
            System.out.println("3. 添加用户");
            System.out.println("4. 修改用户信息");
            System.out.println("5. 删除用户");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    viewUserDetail();
                    break;
                case 3:
                    addUser();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }
    
    private static void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户");
            return;
        }
        System.out.println("\n用户列表:");
        for (User u : users) {
            System.out.printf("%d. %s (%s) - %s\n",
                    u.getId(),
                    u.getRealName(),
                    u.getUsername(),
                    u.getRole().getDescription());
        }
    }
    
    private static void viewUserDetail() {
        System.out.print("请输入用户ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        User u = userService.getUserById(id);
        if (u != null) {
            System.out.println("\n用户ID: " + u.getId());
            System.out.println("用户名: " + u.getUsername());
            System.out.println("真实姓名: " + u.getRealName());
            System.out.println("角色: " + u.getRole().getDescription());
            System.out.println("学号: " + (u.getStudentId() != null ? u.getStudentId() : "无"));
            System.out.println("手机号: " + (u.getPhone() != null ? u.getPhone() : "无"));
            System.out.println("邮箱: " + (u.getEmail() != null ? u.getEmail() : "无"));
        } else {
            System.out.println("用户不存在！");
        }
    }
    
    private static void addUser() {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        System.out.print("请输入真实姓名: ");
        String realName = scanner.nextLine();
        System.out.print("请选择角色 (1-学生, 2-班委, 3-老师): ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        
        User.Role role = User.Role.STUDENT;
        switch (roleChoice) {
            case 1:
                role = User.Role.STUDENT;
                break;
            case 2:
                role = User.Role.COMMITTEE;
                break;
            case 3:
                role = User.Role.TEACHER;
                break;
        }
        
        User user = new User(username, password, realName, role);
        if (userService.register(user)) {
            System.out.println("用户添加成功！");
        } else {
            System.out.println("用户添加失败！");
        }
    }
    
    private static void updateUser() {
        System.out.print("请输入要修改的用户ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        User u = userService.getUserById(id);
        if (u == null) {
            System.out.println("用户不存在！");
            return;
        }
        
        System.out.print("请输入新真实姓名（直接回车保持原值）: ");
        String realName = scanner.nextLine();
        if (!realName.isEmpty()) {
            u.setRealName(realName);
        }
        
        System.out.print("请选择新角色 (1-学生, 2-班委, 3-老师，直接回车保持原值）: ");
        String roleStr = scanner.nextLine();
        if (!roleStr.isEmpty()) {
            int roleChoice = Integer.parseInt(roleStr);
            switch (roleChoice) {
                case 1:
                    u.setRole(User.Role.STUDENT);
                    break;
                case 2:
                    u.setRole(User.Role.COMMITTEE);
                    break;
                case 3:
                    u.setRole(User.Role.TEACHER);
                    break;
            }
        }
        
        if (userService.updateUser(u)) {
            System.out.println("用户信息修改成功！");
        } else {
            System.out.println("用户信息修改失败！");
        }
    }
    
    private static void deleteUser() {
        System.out.print("请输入要删除的用户ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (id == currentUser.getId()) {
            System.out.println("不能删除当前登录用户！");
            return;
        }
        if (userService.deleteUser(id)) {
            System.out.println("用户删除成功！");
        } else {
            System.out.println("用户删除失败！");
        }
    }
}

