package com.classmanagement.dao;

import com.classmanagement.config.DatabaseConfig;
import com.classmanagement.entity.Attendance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 考勤数据访问对象
 */
public class AttendanceDAO {
    
    /**
     * 添加考勤记录
     */
    public boolean addAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendances (user_id, attendance_date, status, remark, recorder_id) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE status = ?, remark = ?, recorder_id = ?, record_time = NOW()";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, attendance.getUserId());
            pstmt.setDate(2, attendance.getAttendanceDate());
            pstmt.setString(3, attendance.getStatus().name());
            pstmt.setString(4, attendance.getRemark());
            pstmt.setInt(5, attendance.getRecorderId());
            pstmt.setString(6, attendance.getStatus().name());
            pstmt.setString(7, attendance.getRemark());
            pstmt.setInt(8, attendance.getRecorderId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 查询指定日期的所有考勤记录
     */
    public List<Attendance> findByDate(Date date) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, u.real_name as user_name, r.real_name as recorder_name " +
                     "FROM attendances a " +
                     "LEFT JOIN users u ON a.user_id = u.id " +
                     "LEFT JOIN users r ON a.recorder_id = r.id " +
                     "WHERE a.attendance_date = ? ORDER BY u.real_name";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, date);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }
    
    /**
     * 查询用户的考勤记录
     */
    public List<Attendance> findByUserId(Integer userId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, u.real_name as user_name, r.real_name as recorder_name " +
                     "FROM attendances a " +
                     "LEFT JOIN users u ON a.user_id = u.id " +
                     "LEFT JOIN users r ON a.recorder_id = r.id " +
                     "WHERE a.user_id = ? ORDER BY a.attendance_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }
    
    /**
     * 查询用户指定日期范围的考勤记录
     */
    public List<Attendance> findByUserIdAndDateRange(Integer userId, Date startDate, Date endDate) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT a.*, u.real_name as user_name, r.real_name as recorder_name " +
                     "FROM attendances a " +
                     "LEFT JOIN users u ON a.user_id = u.id " +
                     "LEFT JOIN users r ON a.recorder_id = r.id " +
                     "WHERE a.user_id = ? AND a.attendance_date BETWEEN ? AND ? " +
                     "ORDER BY a.attendance_date DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }
    
    /**
     * 统计用户考勤情况
     */
    public AttendanceStatistics getStatistics(Integer userId, Date startDate, Date endDate) {
        String sql = "SELECT status, COUNT(*) as count FROM attendances " +
                     "WHERE user_id = ? AND attendance_date BETWEEN ? AND ? " +
                     "GROUP BY status";
        AttendanceStatistics stats = new AttendanceStatistics();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getString("status");
                    int count = rs.getInt("count");
                    switch (status) {
                        case "PRESENT":
                            stats.present = count;
                            break;
                        case "ABSENT":
                            stats.absent = count;
                            break;
                        case "LATE":
                            stats.late = count;
                            break;
                        case "LEAVE":
                            stats.leave = count;
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
    
    /**
     * 将ResultSet映射为Attendance对象
     */
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getInt("id"));
        attendance.setUserId(rs.getInt("user_id"));
        attendance.setAttendanceDate(rs.getDate("attendance_date"));
        attendance.setStatus(Attendance.Status.valueOf(rs.getString("status")));
        attendance.setRemark(rs.getString("remark"));
        attendance.setRecorderId(rs.getInt("recorder_id"));
        attendance.setRecordTime(rs.getTimestamp("record_time"));
        try {
            attendance.setUserName(rs.getString("user_name"));
            attendance.setRecorderName(rs.getString("recorder_name"));
        } catch (SQLException e) {
            // 如果字段不存在，忽略
        }
        return attendance;
    }
    
    /**
     * 考勤统计内部类
     */
    public static class AttendanceStatistics {
        public int present = 0;
        public int absent = 0;
        public int late = 0;
        public int leave = 0;
        
        public int getTotal() {
            return present + absent + late + leave;
        }
    }
}


