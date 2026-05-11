package com.classmanagement.controller;

import com.classmanagement.entity.File;
import com.classmanagement.entity.User;
import com.classmanagement.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 文件控制器
 */
@Controller
@RequestMapping("/file")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    private static final String UPLOAD_DIR = "uploads/";
    
    /**
     * 文件列表
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<File> files = fileService.getAllFiles();
        model.addAttribute("files", files);
        return "file/list";
    }
    
    /**
     * 文件详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        File file = fileService.getFileById(id);
        if (file != null) {
            fileService.incrementDownloadCount(id);
        }
        model.addAttribute("file", file);
        return "file/detail";
    }
    
    /**
     * 上传文件页面
     */
    @GetMapping("/upload")
    public String uploadPage() {
        return "file/upload";
    }
    
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                        @RequestParam(required = false) String description,
                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (file.isEmpty()) {
            return "redirect:/file/upload";
        }
        
        try {
            // 创建上传目录
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String originalName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalName;
            Path filePath = uploadPath.resolve(fileName);
            
            // 保存文件
            Files.write(filePath, file.getBytes());
            
            // 保存文件记录
            File fileEntity = new File(fileName, originalName, filePath.toString(), 
                    file.getSize(), user.getId());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setDescription(description);
            fileService.uploadFile(fileEntity);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "redirect:/file/list";
    }
    
    /**
     * 我的文件
     */
    @GetMapping("/my")
    public String myFiles(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<File> files = fileService.getUserFiles(user.getId());
        model.addAttribute("files", files);
        return "file/my";
    }
    
    /**
     * 删除文件
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole() == User.Role.TEACHER || user.getRole() == User.Role.COMMITTEE) {
            fileService.deleteFile(id);
        }
        return "redirect:/file/list";
    }
}


