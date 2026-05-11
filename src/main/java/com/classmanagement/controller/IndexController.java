package com.classmanagement.controller;

import com.classmanagement.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 首页控制器
 */
@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/home";
        }
        return "redirect:/user/login";
    }
    
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}

