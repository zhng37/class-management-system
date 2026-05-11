package com.classmanagement.service;

import com.classmanagement.dao.ClassFeeDAO;
import com.classmanagement.entity.ClassFee;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 班费服务类
 */
@Service
public class ClassFeeService {
    private ClassFeeDAO classFeeDAO = new ClassFeeDAO();
    
    /**
     * 添加班费记录
     */
    public boolean addClassFee(ClassFee classFee) {
        if (classFee.getUserId() == null || classFee.getAmount() == null ||
            classFee.getType() == null || classFee.getOperatorId() == null) {
            return false;
        }
        
        if (classFee.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        return classFeeDAO.addClassFee(classFee);
    }
    
    /**
     * 获取所有班费记录
     */
    public List<ClassFee> getAllClassFees() {
        return classFeeDAO.findAll();
    }
    
    /**
     * 获取用户的班费记录
     */
    public List<ClassFee> getUserClassFees(Integer userId) {
        return classFeeDAO.findByUserId(userId);
    }
    
    /**
     * 计算班费余额
     */
    public BigDecimal getBalance() {
        return classFeeDAO.calculateBalance();
    }
}


