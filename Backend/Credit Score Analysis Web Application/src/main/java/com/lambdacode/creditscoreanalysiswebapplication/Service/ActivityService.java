package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;

import java.util.List;

public interface ActivityService {

   Object getLatestActivities();

    Object getActivitiesByEmail(String email);

    Object getAllActivities();
}
