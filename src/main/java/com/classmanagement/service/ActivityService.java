package com.classmanagement.service;

import com.classmanagement.dao.ActivityDAO;
import com.classmanagement.dao.ActivityRegistrationDAO;
import com.classmanagement.entity.Activity;
import com.classmanagement.entity.ActivityRegistration;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动服务类
 */
@Service
public class ActivityService {
    private ActivityDAO activityDAO = new ActivityDAO();
    private ActivityRegistrationDAO registrationDAO = new ActivityRegistrationDAO();

    /**
     * 创建活动
     */

    public boolean createActivity(Activity activity) {
        if (activity.getTitle() == null || activity.getTitle().trim().isEmpty() ||
                activity.getStartTime() == null || activity.getOrganizerId() == null) {
            return false;
        }
        return activityDAO.addActivity(activity);
    }

    /**
     * 获取所有活动
     */
    public List<Activity> getAllActivities() {
        return activityDAO.findAll();
    }

    /**
     * 根据ID获取活动
     */
    public Activity getActivityById(Integer id) {
        return activityDAO.findById(id);
    }

    /**
     * 更新活动
     */
    public boolean updateActivity(Activity activity) {

        if (activity.getId() == null) {
            return false;
        }
        return activityDAO.updateActivity(activity);
    }

    /**
     * 删除活动
     */

    public boolean deleteActivity(Integer id) {
        return activityDAO.deleteActivity(id);
    }

    /**
     * 报名活动
     */
    public boolean registerActivity(Integer activityId, Integer userId) {
        Activity activity = activityDAO.findById(activityId);
        if (activity == null) {
            return false;
        }

        // 检查是否已报名
        if (registrationDAO.isRegistered(activityId, userId)) {
            return false;
        }

        // 检查人数限制
        if (activity.getMaxParticipants() != null) {
            Integer current = activity.getCurrentParticipants();
            if (current != null && current >= activity.getMaxParticipants()) {
                return false;
            }
        }

        ActivityRegistration registration = new ActivityRegistration(activityId, userId);
        return registrationDAO.registerActivity(registration);
    }

    /**
     * 签到
     */
    public boolean checkIn(Integer activityId, Integer userId) {
        if (!registrationDAO.isRegistered(activityId, userId)) {
            return false;
        }
        return registrationDAO.checkIn(activityId, userId);
    }

    /**
     * 获取活动的报名列表
     */
    public List<ActivityRegistration> getActivityRegistrations(Integer activityId) {
        return registrationDAO.findByActivityId(activityId);
    }

    /**
     * 获取用户的报名列表
     */
    public List<ActivityRegistration> getUserRegistrations(Integer userId) {
        return registrationDAO.findByUserId(userId);
    }

    /**
     * 取消报名
     */
    public boolean cancelRegistration(Integer activityId, Integer userId) {
        return registrationDAO.cancelRegistration(activityId, userId);
    }
}
