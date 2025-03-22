package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;

import java.util.ArrayList;

public interface RegisterService {
    Object register(RegisterRequest registerRequest);

    Object pendingUserList();
}
