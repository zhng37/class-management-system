package com.classmanagement.entity;

import java.sql.Timestamp;

/**
 * 用户实体类
 */
public class User {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private Role role;
    private String studentId;
    private String phone;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum Role {
        STUDENT("学生"),
        COMMITTEE("班委"),
        TEACHER("老师");

        private String description;

        Role(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public User() {
    }

    public User(String username, String password, String realName, Role role) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.role = role;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", role=" + role +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}


