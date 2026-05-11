package com.classmanagement.entity;

import java.sql.Timestamp;

/**
 * 公告实体类
 */
public class Announcement {
    private Integer id;
    private String title;
    private String content;
    private Integer publisherId;
    private Timestamp publishTime;
    private Boolean isImportant;
    private Status status;
    private String publisherName; // 用于显示发布者姓名

    public enum Status {
        DRAFT("草稿"),
        PUBLISHED("已发布");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Announcement() {
    }

    public Announcement(String title, String content, Integer publisherId) {
        this.title = title;
        this.content = content;
        this.publisherId = publisherId;
        this.status = Status.PUBLISHED;
        this.isImportant = false;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public Boolean getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Boolean isImportant) {
        this.isImportant = isImportant;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}


