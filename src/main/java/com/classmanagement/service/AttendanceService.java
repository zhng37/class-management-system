package com.classmanagement.service;

import com.classmanagement.dao.AttendanceDAO;
import com.classmanagement.entity.Attendance;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * 考勤服务类
 */
@Service
public class AttendanceService {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    
    /**
     * 记录考勤
     */
    public boolean recordAttendance(Attendance attendance) {
        if (attendance.getUserId() == null || attendance.getAttendanceDate() == null ||
            attendance.getStatus() == null || attendance.getRecorderId() == null) {
            return false;
        }
        return attendanceDAO.addAttendance(attendance);
    }
    
    /**
     * 获取指定日期的考勤记录
     */
    public List<Attendance> getAttendanceByDate(Date date) {
        return attendanceDAO.findByDate(date);
    }
    
    /**
     * 获取用户的考勤记录
     */
    public List<Attendance> getUserAttendance(Integer userId) {
        return attendanceDAO.findByUserId(userId);
    }
    
    /**
     * 获取用户指定日期范围的考勤记录
     */
    public List<Attendance> getUserAttendanceByDateRange(Integer userId, Date startDate, Date endDate) {
        return attendanceDAO.findByUserIdAndDateRange(userId, startDate, endDate);
    }
    
    /**
     * 获取考勤统计
     */
    public AttendanceDAO.AttendanceStatistics getStatistics(Integer userId, Date startDate, Date endDate) {
        return attendanceDAO.getStatistics(userId, startDate, endDate);
    }
}


