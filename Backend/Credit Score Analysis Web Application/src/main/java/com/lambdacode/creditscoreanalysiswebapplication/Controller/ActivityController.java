package com.lambdacode.creditscoreanalysiswebapplication.Controller;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/latest")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Object> getLatestActivities() {
        return ResponseEntity.ok(activityService.getLatestActivities());
    }

    @GetMapping("/byEmail/{email}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Object> getActivitiesByEmail(@PathVariable String email) {
        return ResponseEntity.ok(activityService.getActivitiesByEmail(email));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }
}
