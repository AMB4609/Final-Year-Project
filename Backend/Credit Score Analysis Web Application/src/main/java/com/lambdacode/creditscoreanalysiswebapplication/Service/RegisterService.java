package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Request.ApproveRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RejectRequest;

import java.util.ArrayList;

public interface RegisterService {
    Object register(RegisterRequest registerRequest);

    Object pendingUserList();

    Object approveRequest(ApproveRequest approveRequest);

    Object rejectRequest(RejectRequest rejectRequest);
}
