package com.classmanagement.controller;

import com.classmanagement.entity.User;
import com.classmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/home";
        }
        return "login";
    }
    
    /**
     * 登录处理
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "用户名或密码错误！");
            return "login";
        }
    }
    
    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    /**
     * 注册处理
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String realName,
                          @RequestParam String role,
                          @RequestParam(required = false) String studentId,
                          @RequestParam(required = false) String phone,
                          @RequestParam(required = false) String email,
                          Model model) {
        User.Role userRole = User.Role.valueOf(role);
        User user = new User(username, password, realName, userRole);
        user.setStudentId(studentId != null && !studentId.isEmpty() ? studentId : null);
        user.setPhone(phone != null && !phone.isEmpty() ? phone : null);
        user.setEmail(email != null && !email.isEmpty() ? email : null);
        
        if (userService.register(user)) {
            model.addAttribute("success", "注册成功！请登录");
            return "login";
        } else {
            model.addAttribute("error", "注册失败！用户名可能已存在");
            return "register";
        }
    }
    
    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    /**
     * 用户管理页面（仅班委和老师）
     */
    @GetMapping("/manage")
    public String userManage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRole() != User.Role.TEACHER && currentUser.getRole() != User.Role.COMMITTEE) {
            return "redirect:/home";
        }
        
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/manage";
    }
    
    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRole() != User.Role.TEACHER && currentUser.getRole() != User.Role.COMMITTEE) {
            return "redirect:/home";
        }
        
        if (!id.equals(currentUser.getId())) {
            userService.deleteUser(id);
        }
        return "redirect:/user/manage";
    }
}

