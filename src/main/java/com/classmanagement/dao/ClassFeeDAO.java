package com.classmanagement.dao;

import com.classmanagement.config.DatabaseConfig;
import com.classmanagement.entity.ClassFee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 班费数据访问对象
 */
public class ClassFeeDAO {
    
    /**
     * 添加班费记录
     */
    public boolean addClassFee(ClassFee classFee) {
        String sql = "INSERT INTO class_fees (user_id, amount, type, description, operator_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, classFee.getUserId());
            pstmt.setBigDecimal(2, classFee.getAmount());
            pstmt.setString(3, classFee.getType().name());
            pstmt.setString(4, classFee.getDescription());
            pstmt.setInt(5, classFee.getOperatorId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 查询所有班费记录
     */
    public List<ClassFee> findAll() {
        List<ClassFee> fees = new ArrayList<>();
        String sql = "SELECT cf.*, u.real_name as user_name, o.real_name as operator_name " +
                     "FROM class_fees cf " +
                     "LEFT JOIN users u ON cf.user_id = u.id " +
                     "LEFT JOIN users o ON cf.operator_id = o.id " +
                     "ORDER BY cf.transaction_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                fees.add(mapResultSetToClassFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fees;
    }
    
    /**
     * 根据用户ID查询班费记录
     */
    public List<ClassFee> findByUserId(Integer userId) {
        List<ClassFee> fees = new ArrayList<>();
        String sql = "SELECT cf.*, u.real_name as user_name, o.real_name as operator_name " +
                     "FROM class_fees cf " +
                     "LEFT JOIN users u ON cf.user_id = u.id " +
                     "LEFT JOIN users o ON cf.operator_id = o.id " +
                     "WHERE cf.user_id = ? ORDER BY cf.transaction_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fees.add(mapResultSetToClassFee(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fees;
    }
    
    /**
     * 计算班费余额
     */
    public BigDecimal calculateBalance() {
        String sql = "SELECT SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) as balance " +
                     "FROM class_fees";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal balance = rs.getBigDecimal("balance");
                return balance != null ? balance : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 将ResultSet映射为ClassFee对象
     */
    private ClassFee mapResultSetToClassFee(ResultSet rs) throws SQLException {
        ClassFee classFee = new ClassFee();
        classFee.setId(rs.getInt("id"));
        classFee.setUserId(rs.getInt("user_id"));
        classFee.setAmount(rs.getBigDecimal("amount"));
        classFee.setType(ClassFee.Type.valueOf(rs.getString("type")));
        classFee.setDescription(rs.getString("description"));
        classFee.setOperatorId(rs.getInt("operator_id"));
        classFee.setTransactionTime(rs.getTimestamp("transaction_time"));
        try {
            classFee.setUserName(rs.getString("user_name"));
            classFee.setOperatorName(rs.getString("operator_name"));
        } catch (SQLException e) {
            // 如果字段不存在，忽略
        }
        return classFee;
    }
}


