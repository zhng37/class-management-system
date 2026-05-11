package com.classmanagement.entity;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 考勤实体类
 */
public class Attendance {
    private Integer id;
    private Integer userId;
    private Date attendanceDate;
    private Status status;
    private String remark;
    private Integer recorderId;
    private Timestamp recordTime;
    private String userName; // 用于显示用户名
    private String recorderName; // 用于显示记录员姓名

    public enum Status {
        PRESENT("出勤"),
        ABSENT("缺勤"),
        LATE("迟到"),
        LEAVE("请假");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Attendance() {
    }

    public Attendance(Integer userId, Date attendanceDate, Status status, Integer recorderId) {
        this.userId = userId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.recorderId = recorderId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRecorderId() {
        return recorderId;
    }

    public void setRecorderId(Integer recorderId) {
        this.recorderId = recorderId;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }
}


