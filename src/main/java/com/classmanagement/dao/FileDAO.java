package com.classmanagement.dao;

import com.classmanagement.config.DatabaseConfig;
import com.classmanagement.entity.File;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件数据访问对象
 */
public class FileDAO {
    
    /**
     * 添加文件记录
     */
    public boolean addFile(File file) {
        String sql = "INSERT INTO files (file_name, original_name, file_path, file_size, file_type, " +
                     "uploader_id, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, file.getFileName());
            pstmt.setString(2, file.getOriginalName());
            pstmt.setString(3, file.getFilePath());
            pstmt.setLong(4, file.getFileSize());
            pstmt.setString(5, file.getFileType());
            pstmt.setInt(6, file.getUploaderId());
            pstmt.setString(7, file.getDescription());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 根据ID查询文件
     */
    public File findById(Integer id) {
        String sql = "SELECT f.*, u.real_name as uploader_name FROM files f " +
                     "LEFT JOIN users u ON f.uploader_id = u.id WHERE f.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFile(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询所有文件
     */
    public List<File> findAll() {
        List<File> files = new ArrayList<>();
        String sql = "SELECT f.*, u.real_name as uploader_name FROM files f " +
                     "LEFT JOIN users u ON f.uploader_id = u.id " +
                     "ORDER BY f.upload_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                files.add(mapResultSetToFile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }
    
    /**
     * 根据上传者查询文件
     */
    public List<File> findByUploaderId(Integer uploaderId) {
        List<File> files = new ArrayList<>();
        String sql = "SELECT f.*, u.real_name as uploader_name FROM files f " +
                     "LEFT JOIN users u ON f.uploader_id = u.id " +
                     "WHERE f.uploader_id = ? ORDER BY f.upload_time DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, uploaderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFile(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }
    
    /**
     * 增加下载次数
     */
    public boolean incrementDownloadCount(Integer id) {
        String sql = "UPDATE files SET download_count = download_count + 1 WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 删除文件记录
     */
    public boolean deleteFile(Integer id) {
        String sql = "DELETE FROM files WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 将ResultSet映射为File对象
     */
    private File mapResultSetToFile(ResultSet rs) throws SQLException {
        File file = new File();
        file.setId(rs.getInt("id"));
        file.setFileName(rs.getString("file_name"));
        file.setOriginalName(rs.getString("original_name"));
        file.setFilePath(rs.getString("file_path"));
        file.setFileSize(rs.getLong("file_size"));
        file.setFileType(rs.getString("file_type"));
        file.setUploaderId(rs.getInt("uploader_id"));
        file.setUploadTime(rs.getTimestamp("upload_time"));
        file.setDescription(rs.getString("description"));
        file.setDownloadCount(rs.getInt("download_count"));
        try {
            file.setUploaderName(rs.getString("uploader_name"));
        } catch (SQLException e) {
            // 如果字段不存在，忽略
        }
        return file;
    }
}


