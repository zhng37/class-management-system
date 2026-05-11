package com.classmanagement.service;

import com.classmanagement.dao.UserDAO;
import com.classmanagement.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务类
 */
@Service
public class UserService {
    private UserDAO userDAO = new UserDAO();
    
    /**
     * 用户登录
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDAO.login(username, password);
    }
    
    /**
     * 用户注册
     */
    public boolean register(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty() ||
            user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            return false;
        }
        
        // 检查用户名是否已存在
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return false;
        }
        
        return userDAO.addUser(user);
    }
    
    /**
     * 根据ID获取用户
     */
    public User getUserById(Integer id) {
        return userDAO.findById(id);
    }
    
    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    /**
     * 根据角色获取用户
     */
    public List<User> getUsersByRole(User.Role role) {
        return userDAO.findByRole(role);
    }
    
    /**
     * 更新用户信息
     */
    public boolean updateUser(User user) {
        if (user.getId() == null) {
            return false;
        }
        return userDAO.updateUser(user);
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer id) {
        return userDAO.deleteUser(id);
    }
}


