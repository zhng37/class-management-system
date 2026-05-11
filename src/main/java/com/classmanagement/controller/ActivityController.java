package com.classmanagement.controller;

import com.classmanagement.entity.Activity;
import com.classmanagement.entity.ActivityRegistration;
import com.classmanagement.entity.User;
import com.classmanagement.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

/**
 * 活动控制器
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    /**
     * 活动列表
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        List<Activity> activities = activityService.getAllActivities();
        User user = (User) session.getAttribute("user");
        model.addAttribute("activities", activities);
        model.addAttribute("currentUser", user);
        return "activity/list";
    }
    
    /**
     * 活动详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpSession session) {
        Activity activity = activityService.getActivityById(id);
        User user = (User) session.getAttribute("user");
        boolean isRegistered = false;
        if (user != null) {
            isRegistered = activityService.getUserRegistrations(user.getId()).stream()
                    .anyMatch(r -> r.getActivityId().equals(id));
        }
        model.addAttribute("activity", activity);
        model.addAttribute("isRegistered", isRegistered);
        model.addAttribute("currentUser", user);
        return "activity/detail";
    }
    
    /**
     * 创建活动页面
     */
    @GetMapping("/create")
    public String createPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/activity/list";
        }
        return "activity/create";
    }
    
    /**
     * 创建活动
     */
    @PostMapping("/create")
    public String create(@RequestParam String title,
                        @RequestParam String description,
                        @RequestParam(required = false) String location,
                        @RequestParam String startTime,
                        @RequestParam(required = false) String endTime,
                        @RequestParam(required = false) Integer maxParticipants,
                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/activity/list";
        }
        
        try {
            Timestamp start = Timestamp.valueOf(startTime + ":00");
            Timestamp end = endTime != null && !endTime.isEmpty() ? Timestamp.valueOf(endTime + ":00") : null;
            Activity activity = new Activity(title, description, start, user.getId());
            activity.setLocation(location);
            activity.setEndTime(end);
            activity.setMaxParticipants(maxParticipants);
            activityService.createActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/activity/list";
    }
    
    /**
     * 报名活动
     */
    @PostMapping("/register/{id}")
    public String register(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            activityService.registerActivity(id, user.getId());
        }
        return "redirect:/activity/detail/" + id;
    }
    
    /**
     * 取消报名
     */
    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            activityService.cancelRegistration(id, user.getId());
        }
        return "redirect:/activity/detail/" + id;
    }
    
    /**
     * 签到
     */
    @PostMapping("/checkin/{activityId}/{userId}")
    public String checkIn(@PathVariable Integer activityId, @PathVariable Integer userId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() == User.Role.TEACHER || user.getRole() == User.Role.COMMITTEE) {
            activityService.checkIn(activityId, userId);
        }
        return "redirect:/activity/detail/" + activityId;
    }
    
    /**
     * 报名列表
     */
    @GetMapping("/registrations/{id}")
    public String registrations(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/activity/list";
        }
        
        List<ActivityRegistration> registrations = activityService.getActivityRegistrations(id);
        Activity activity = activityService.getActivityById(id);
        model.addAttribute("registrations", registrations);
        model.addAttribute("activity", activity);
        return "activity/registrations";
    }
}


