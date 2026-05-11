package com.classmanagement.controller;

import com.classmanagement.entity.ClassFee;
import com.classmanagement.entity.User;
import com.classmanagement.service.ClassFeeService;
import com.classmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * 班费控制器
 */
@Controller
@RequestMapping("/fee")
public class ClassFeeController {
    
    @Autowired
    private ClassFeeService classFeeService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 班费明细列表
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<ClassFee> fees = classFeeService.getAllClassFees();
        BigDecimal balance = classFeeService.getBalance();
        model.addAttribute("fees", fees);
        model.addAttribute("balance", balance);
        return "fee/list";
    }
    
    /**
     * 记录收入页面
     */
    @GetMapping("/income")
    public String incomePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/fee/list";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "fee/income";
    }
    
    /**
     * 记录收入
     */
    @PostMapping("/income")
    public String income(@RequestParam Integer userId,
                        @RequestParam BigDecimal amount,
                        @RequestParam(required = false) String description,
                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/fee/list";
        }
        
        ClassFee fee = new ClassFee(userId, amount, ClassFee.Type.INCOME, user.getId());
        fee.setDescription(description);
        classFeeService.addClassFee(fee);
        return "redirect:/fee/list";
    }
    
    /**
     * 记录支出页面
     */
    @GetMapping("/expense")
    public String expensePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/fee/list";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "fee/expense";
    }
    
    /**
     * 记录支出
     */
    @PostMapping("/expense")
    public String expense(@RequestParam Integer userId,
                         @RequestParam BigDecimal amount,
                         @RequestParam(required = false) String description,
                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.TEACHER && user.getRole() != User.Role.COMMITTEE) {
            return "redirect:/fee/list";
        }
        
        ClassFee fee = new ClassFee(userId, amount, ClassFee.Type.EXPENSE, user.getId());
        fee.setDescription(description);
        classFeeService.addClassFee(fee);
        return "redirect:/fee/list";
    }
}


