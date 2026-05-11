package com.classmanagement.service;

import com.classmanagement.dao.AnnouncementDAO;
import com.classmanagement.entity.Announcement;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告服务类
 */
@Service
public class AnnouncementService {
    private AnnouncementDAO announcementDAO = new AnnouncementDAO();
    
    /**
     * 发布公告
     */
    public boolean publishAnnouncement(Announcement announcement) {
        if (announcement.getTitle() == null || announcement.getTitle().trim().isEmpty() ||
            announcement.getContent() == null || announcement.getContent().trim().isEmpty() ||
            announcement.getPublisherId() == null) {
            return false;
        }
        return announcementDAO.addAnnouncement(announcement);
    }
    
    /**
     * 获取所有已发布的公告
     */
    public List<Announcement> getAllPublishedAnnouncements() {
        return announcementDAO.findAllPublished();
    }
    
    /**
     * 根据ID获取公告
     */
    public Announcement getAnnouncementById(Integer id) {
        return announcementDAO.findById(id);
    }
    
    /**
     * 更新公告
     */
    public boolean updateAnnouncement(Announcement announcement) {
        if (announcement.getId() == null) {
            return false;
        }
        return announcementDAO.updateAnnouncement(announcement);
    }
    
    /**
     * 删除公告
     */
    public boolean deleteAnnouncement(Integer id) {
        return announcementDAO.deleteAnnouncement(id);
    }
}


