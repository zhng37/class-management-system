package com.classmanagement.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 班费实体类
 */
public class ClassFee {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private Type type;
    private String description;
    private Integer operatorId;
    private Timestamp transactionTime;
    private String userName; // 用于显示用户名
    private String operatorName; // 用于显示操作员姓名

    public enum Type {
        INCOME("收入"),
        EXPENSE("支出");

        private String description;

        Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public ClassFee() {
    }

    public ClassFee(Integer userId, BigDecimal amount, Type type, Integer operatorId) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.operatorId = operatorId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}


