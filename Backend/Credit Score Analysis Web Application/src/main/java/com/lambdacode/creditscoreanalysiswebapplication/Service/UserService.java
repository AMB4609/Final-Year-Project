package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Request.EditRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.UserIdRequest;

import java.io.IOException;

public interface UserService {


    Object deleteUser(String UserEmail,String PerformedByEmail);

    Object getUserByEmail(String userEmail);
    Object getAllUsers(int page, int size);

    Object editUser(EditRequest editRequest) throws IOException;

    Object getAllStaff(int page, int size);
    Object changeUserRole(String userEmail, String newRole) throws IOException;

}
