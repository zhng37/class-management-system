package com.classmanagement.entity;

import java.sql.Timestamp;

/**
 * 活动实体类
 */
public class Activity {
    private Integer id;
    private String title;
    private String description;
    private String location;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer organizerId;
    private Integer maxParticipants;
    private Status status;
    private Timestamp createdAt;
    private String organizerName; // 用于显示组织者姓名
    private Integer currentParticipants; // 当前报名人数

    public enum Status {
        REGISTERING("报名中"),
        ONGOING("进行中"),
        COMPLETED("已完成"),
        CANCELLED("已取消");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Activity() {
    }

    public Activity(String title, String description, Timestamp startTime, Integer organizerId) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.organizerId = organizerId;
        this.status = Status.REGISTERING;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Integer getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Integer organizerId) {
        this.organizerId = organizerId;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }
}


