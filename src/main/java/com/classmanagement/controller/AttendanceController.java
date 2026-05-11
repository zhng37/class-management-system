package com.classmanagement.controller;

import com.classmanagement.entity.Attendance;
import com.classmanagement.entity.User;
import com.classmanagement.service.AttendanceService;
import com.classmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

/**
 * 考勤控制器
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 我的考勤记录
     */
    @GetMapping("/my")
    public String myAttendance(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Attendance> attendances = attendanceService.getUserAttendance(user.getId());
        model.addAttribute("attendances", attendances);
        return "attendance/my";
    }
    
    /**
     * 记录考勤页面（仅班委和老师）
     */
    @GetMapping("/record")
    public String recordPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/attendance/my";
        }
        
        List<User> students = userService.getUsersByRole(User.Role.STUDENT);
        model.addAttribute("students", students);
        return "attendance/record";
    }
    
    /**
     * 记录考勤
     */
    @PostMapping("/record")
    public String record(@RequestParam Integer userId,
                       @RequestParam String date,
                       @RequestParam String status,
                       @RequestParam(required = false) String remark,
                       HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/attendance/my";
        }
        
        try {
            Date attendanceDate = Date.valueOf(date);
            Attendance.Status attendanceStatus = Attendance.Status.valueOf(status);
            Attendance attendance = new Attendance(userId, attendanceDate, attendanceStatus, user.getId());
            attendance.setRemark(remark);
            attendanceService.recordAttendance(attendance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/attendance/record";
    }
    
    /**
     * 按日期查看考勤
     */
    @GetMapping("/date")
    public String byDate(@RequestParam(required = false) String date, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/attendance/my";
        }
        
        if (date != null && !date.isEmpty()) {
            try {
                Date attendanceDate = Date.valueOf(date);
                List<Attendance> attendances = attendanceService.getAttendanceByDate(attendanceDate);
                model.addAttribute("attendances", attendances);
                model.addAttribute("selectedDate", date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "attendance/date";
    }
}


