package com.classmanagement.entity;

import java.sql.Timestamp;

/**
 * 活动报名实体类
 */
public class ActivityRegistration {
    private Integer id;
    private Integer activityId;
    private Integer userId;
    private Timestamp registrationTime;
    private Boolean isCheckedIn;
    private Timestamp checkInTime;
    private String userName; // 用于显示用户名
    private String activityTitle; // 用于显示活动标题

    public ActivityRegistration() {
    }

    public ActivityRegistration(Integer activityId, Integer userId) {
        this.activityId = activityId;
        this.userId = userId;
        this.isCheckedIn = false;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Boolean getIsCheckedIn() {
        return isCheckedIn;
    }

    public void setIsCheckedIn(Boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }

    public Timestamp getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Timestamp checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }
}


