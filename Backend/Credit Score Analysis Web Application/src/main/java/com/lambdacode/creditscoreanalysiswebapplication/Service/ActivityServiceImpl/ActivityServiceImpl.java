package com.lambdacode.creditscoreanalysiswebapplication.Service.ActivityServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.ActivityRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityRepository activityRepository;

    protected JwtResponse setResponseFields(Object data, int code, String message, boolean status) {
        JwtResponse dto = new JwtResponse();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setStatus(status);
        dto.setData(data);
        return dto;
    }
    public Object getLatestActivities() {
        List<Activity> activities = activityRepository.findTop5ByOrderByActivityTimeDesc();
        return setResponseFields(activities, 200, "Latest activities fetched successfully", true);
    }

    public Object getActivitiesByEmail(String email) {
        List<Activity> userActivities = activityRepository.findByUserEmail(email);
        List<Activity> staffActivities = activityRepository.findByStaffEmail(email);

        userActivities.addAll(staffActivities); // Merge user + staff
        return setResponseFields(userActivities, 200, "Activities fetched for email successfully", true);
    }

    public Object getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return setResponseFields(activities, 200, "All activities fetched successfully", true);
    }
}
