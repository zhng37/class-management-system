package com.classmanagement.controller;

import com.classmanagement.entity.Announcement;
import com.classmanagement.entity.User;
import com.classmanagement.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 公告控制器
 */
@Controller
@RequestMapping("/announcement")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementService announcementService;
    
    /**
     * 公告列表
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Announcement> announcements = announcementService.getAllPublishedAnnouncements();
        model.addAttribute("announcements", announcements);
        return "announcement/list";
    }
    
    /**
     * 公告详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        model.addAttribute("announcement", announcement);
        return "announcement/detail";
    }
    
    /**
     * 发布公告页面（仅班委和老师）
     */
    @GetMapping("/publish")
    public String publishPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/announcement/list";
        }
        return "announcement/publish";
    }
    
    /**
     * 发布公告
     */
    @PostMapping("/publish")
    public String publish(@RequestParam String title,
                         @RequestParam String content,
                         @RequestParam(required = false, defaultValue = "false") Boolean isImportant,
                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/announcement/list";
        }
        
        Announcement announcement = new Announcement(title, content, user.getId());
        announcement.setIsImportant(isImportant != null && isImportant);
        announcementService.publishAnnouncement(announcement);
        return "redirect:/announcement/list";
    }
    
    /**
     * 删除公告
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/announcement/list";
        }
        
        announcementService.deleteAnnouncement(id);
        return "redirect:/announcement/list";
    }
}


