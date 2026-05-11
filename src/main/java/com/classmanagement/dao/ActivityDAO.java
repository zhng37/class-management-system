package com.classmanagement.dao;

import com.classmanagement.config.DatabaseConfig;
import com.classmanagement.entity.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动数据访问对象
 */
public class ActivityDAO {
    
    /**
     * 添加活动
     */
    public boolean addActivity(Activity activity) {
        String sql = "INSERT INTO activities (title, description, location, start_time, end_time, " +
                     "organizer_id, max_participants, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, activity.getTitle());
            pstmt.setString(2, activity.getDescription());
            pstmt.setString(3, activity.getLocation());
            pstmt.setTimestamp(4, activity.getStartTime());
            pstmt.setTimestamp(5, activity.getEndTime());
            pstmt.setInt(6, activity.getOrganizerId());
            pstmt.setObject(7, activity.getMaxParticipants());
            pstmt.setString(8, activity.getStatus().name());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 根据ID查询活动
     */
    public Activity findById(Integer id) {
        String sql = "SELECT a.*, u.real_name as organizer_name, " +
                     "(SELECT COUNT(*) FROM activity_registrations WHERE activity_id = a.id) as current_participants " +
                     "FROM activities a " +
                     "LEFT JOIN users u ON a.organizer_id = u.id WHERE a.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActivity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询所有活动
     */
    public List<Activity> findAll() {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT a.*, u.real_name as organizer_name, " +
                     "(SELECT COUNT(*) FROM activity_registrations WHERE activity_id = a.id) as current_participants " +
                     "FROM activities a " +
                     "LEFT JOIN users u ON a.organizer_id = u.id " +
                     "ORDER BY a.start_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }
    
    /**
     * 更新活动
     */
    public boolean updateActivity(Activity activity) {
        String sql = "UPDATE activities SET title = ?, description = ?, location = ?, start_time = ?, " +
                     "end_time = ?, max_participants = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, activity.getTitle());
            pstmt.setString(2, activity.getDescription());
            pstmt.setString(3, activity.getLocation());
            pstmt.setTimestamp(4, activity.getStartTime());
            pstmt.setTimestamp(5, activity.getEndTime());
            pstmt.setObject(6, activity.getMaxParticipants());
            pstmt.setString(7, activity.getStatus().name());
            pstmt.setInt(8, activity.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 删除活动
     */
    public boolean deleteActivity(Integer id) {
        String sql = "DELETE FROM activities WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 将ResultSet映射为Activity对象
     */
    private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
        Activity activity = new Activity();
        activity.setId(rs.getInt("id"));
        activity.setTitle(rs.getString("title"));
        activity.setDescription(rs.getString("description"));
        activity.setLocation(rs.getString("location"));
        activity.setStartTime(rs.getTimestamp("start_time"));
        activity.setEndTime(rs.getTimestamp("end_time"));
        activity.setOrganizerId(rs.getInt("organizer_id"));
        activity.setMaxParticipants(rs.getInt("max_participants"));
        if (rs.wasNull()) {
            activity.setMaxParticipants(null);
        }
        activity.setStatus(Activity.Status.valueOf(rs.getString("status")));
        activity.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            activity.setOrganizerName(rs.getString("organizer_name"));
            activity.setCurrentParticipants(rs.getInt("current_participants"));
        } catch (SQLException e) {
            // 如果字段不存在，忽略
        }
        return activity;
    }
}


