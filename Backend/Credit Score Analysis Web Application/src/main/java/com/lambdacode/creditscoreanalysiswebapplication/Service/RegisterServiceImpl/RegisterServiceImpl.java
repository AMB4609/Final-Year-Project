package com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.EmailService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {
    private JwtResponse setResponseFields(Object data, int code, String message, boolean status) {
        JwtResponse dto = new JwtResponse();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setStatus(status);
        dto.setData(data);
        return dto;
    }
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmailService emailService;

    @Override
    public Object register(RegisterRequest registerRequest) {
        List<String> staffEmails = Arrays.asList(
                "amoghbajracharya@gmail.com"
        );
        // Send registration details to staff
        // Send registration request to staff for approval
        emailService.sendRegistrationApprovalRequest(staffEmails, registerRequest);
        User user = new User();
        user.setUserEmail(registerRequest.getUserEmail());
        user.setUserName(registerRequest.getUserName());
        user.setUserPassword(encoder.encode(registerRequest.getUserPassword()));
        user.setStatus(false);
        user.setRegistrationDate(LocalDate.now());
        userRepository.save(user);
        return setResponseFields(null,200,"Registration request sent to staff for approval!",true);
    }

    @Override
    public Object pendingUserList() {
        List<User>  userList = userRepository.findByStatusFalse();
        return setResponseFields(userList,200,"Pending User List Generated",true);
    }
}
