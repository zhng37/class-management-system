package com.classmanagement.service;

import com.classmanagement.dao.FileDAO;
import com.classmanagement.entity.File;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件服务类
 */
@Service
public class FileService {
    private FileDAO fileDAO = new FileDAO();
    
    /**
     * 上传文件（添加文件记录）
     */
    public boolean uploadFile(File file) {
        if (file.getFileName() == null || file.getFileName().trim().isEmpty() ||
            file.getOriginalName() == null || file.getOriginalName().trim().isEmpty() ||
            file.getFilePath() == null || file.getFilePath().trim().isEmpty() ||
            file.getFileSize() == null || file.getUploaderId() == null) {
            return false;
        }
        return fileDAO.addFile(file);
    }
    
    /**
     * 获取所有文件
     */
    public List<File> getAllFiles() {
        return fileDAO.findAll();
    }
    
    /**
     * 根据ID获取文件
     */
    public File getFileById(Integer id) {
        return fileDAO.findById(id);
    }
    
    /**
     * 获取用户上传的文件
     */
    public List<File> getUserFiles(Integer uploaderId) {
        return fileDAO.findByUploaderId(uploaderId);
    }
    
    /**
     * 增加下载次数
     */
    public boolean incrementDownloadCount(Integer id) {
        return fileDAO.incrementDownloadCount(id);
    }
    
    /**
     * 删除文件
     */
    public boolean deleteFile(Integer id) {
        return fileDAO.deleteFile(id);
    }
}


