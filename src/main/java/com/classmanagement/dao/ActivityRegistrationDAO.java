package com.classmanagement.dao;

import com.classmanagement.config.DatabaseConfig;
import com.classmanagement.entity.ActivityRegistration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动报名数据访问对象
 */
public class ActivityRegistrationDAO {
    
    /**
     * 报名活动
     */
    public boolean registerActivity(ActivityRegistration registration) {
        String sql = "INSERT INTO activity_registrations (activity_id, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, registration.getActivityId());
            pstmt.setInt(2, registration.getUserId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // 如果是唯一约束冲突，说明已经报名
            if (e.getErrorCode() == 1062) {
                return false;
            }
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 签到
     */
    public boolean checkIn(Integer activityId, Integer userId) {
        String sql = "UPDATE activity_registrations SET is_checked_in = TRUE, check_in_time = NOW() " +
                     "WHERE activity_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 查询活动的所有报名记录
     */
    public List<ActivityRegistration> findByActivityId(Integer activityId) {
        List<ActivityRegistration> registrations = new ArrayList<>();
        String sql = "SELECT ar.*, u.real_name as user_name, a.title as activity_title " +
                     "FROM activity_registrations ar " +
                     "LEFT JOIN users u ON ar.user_id = u.id " +
                     "LEFT JOIN activities a ON ar.activity_id = a.id " +
                     "WHERE ar.activity_id = ? ORDER BY ar.registration_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, activityId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    registrations.add(mapResultSetToRegistration(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrations;
    }
    
    /**
     * 查询用户的所有报名记录
     */
    public List<ActivityRegistration> findByUserId(Integer userId) {
        List<ActivityRegistration> registrations = new ArrayList<>();
        String sql = "SELECT ar.*, u.real_name as user_name, a.title as activity_title " +
                     "FROM activity_registrations ar " +
                     "LEFT JOIN users u ON ar.user_id = u.id " +
                     "LEFT JOIN activities a ON ar.activity_id = a.id " +
                     "WHERE ar.user_id = ? ORDER BY ar.registration_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    registrations.add(mapResultSetToRegistration(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrations;
    }
    
    /**
     * 检查用户是否已报名
     */
    public boolean isRegistered(Integer activityId, Integer userId) {
        String sql = "SELECT COUNT(*) FROM activity_registrations WHERE activity_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 取消报名
     */
    public boolean cancelRegistration(Integer activityId, Integer userId) {
        String sql = "DELETE FROM activity_registrations WHERE activity_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, activityId);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 将ResultSet映射为ActivityRegistration对象
     */
    private ActivityRegistration mapResultSetToRegistration(ResultSet rs) throws SQLException {
        ActivityRegistration registration = new ActivityRegistration();
        registration.setId(rs.getInt("id"));
        registration.setActivityId(rs.getInt("activity_id"));
        registration.setUserId(rs.getInt("user_id"));
        registration.setRegistrationTime(rs.getTimestamp("registration_time"));
        registration.setIsCheckedIn(rs.getBoolean("is_checked_in"));
        registration.setCheckInTime(rs.getTimestamp("check_in_time"));
        try {
            registration.setUserName(rs.getString("user_name"));
            registration.setActivityTitle(rs.getString("activity_title"));
        } catch (SQLException e) {
            // 如果字段不存在，忽略
        }
        return registration;
    }
}


