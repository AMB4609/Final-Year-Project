package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;

public interface AuthHeaderService {

    User getUserId(String authHeader);
}
