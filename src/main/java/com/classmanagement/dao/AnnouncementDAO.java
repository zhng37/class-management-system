package com.classmanagement.dao;

import com.classmanagement.config.DatabaseConfig;
import com.classmanagement.entity.Announcement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 公告数据访问对象
 */
public class AnnouncementDAO {
    
    /**
     * 添加公告
     */
    public boolean addAnnouncement(Announcement announcement) {
        String sql = "INSERT INTO announcements (title, content, publisher_id, is_important, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getContent());
            pstmt.setInt(3, announcement.getPublisherId());
            pstmt.setBoolean(4, announcement.getIsImportant());
            pstmt.setString(5, announcement.getStatus().name());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 根据ID查询公告
     */
    public Announcement findById(Integer id) {
        String sql = "SELECT a.*, u.real_name as publisher_name FROM announcements a " +
                     "LEFT JOIN users u ON a.publisher_id = u.id WHERE a.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAnnouncement(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询所有已发布的公告
     */
    public List<Announcement> findAllPublished() {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT a.*, u.real_name as publisher_name FROM announcements a " +
                     "LEFT JOIN users u ON a.publisher_id = u.id " +
                     "WHERE a.status = 'PUBLISHED' ORDER BY a.is_important DESC, a.publish_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                announcements.add(mapResultSetToAnnouncement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return announcements;
    }
    
    /**
     * 更新公告
     */
    public boolean updateAnnouncement(Announcement announcement) {
        String sql = "UPDATE announcements SET title = ?, content = ?, is_important = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getContent());
            pstmt.setBoolean(3, announcement.getIsImportant());
            pstmt.setString(4, announcement.getStatus().name());
            pstmt.setInt(5, announcement.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 删除公告
     */
    public boolean deleteAnnouncement(Integer id) {
        String sql = "DELETE FROM announcements WHERE id = ?";
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
     * 将ResultSet映射为Announcement对象
     */
    private Announcement mapResultSetToAnnouncement(ResultSet rs) throws SQLException {
        Announcement announcement = new Announcement();
        announcement.setId(rs.getInt("id"));
        announcement.setTitle(rs.getString("title"));
        announcement.setContent(rs.getString("content"));
        announcement.setPublisherId(rs.getInt("publisher_id"));
        announcement.setPublishTime(rs.getTimestamp("publish_time"));
        announcement.setIsImportant(rs.getBoolean("is_important"));
        announcement.setStatus(Announcement.Status.valueOf(rs.getString("status")));
        try {
            announcement.setPublisherName(rs.getString("publisher_name"));
        } catch (SQLException e) {
            // 如果字段不存在，忽略
        }
        return announcement;
    }
}


