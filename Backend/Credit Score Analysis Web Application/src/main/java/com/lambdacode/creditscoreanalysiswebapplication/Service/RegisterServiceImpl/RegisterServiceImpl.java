package com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Model.Staff;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.ActivityRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.StaffRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.ApproveRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RejectRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.EmailService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private ActivityRepository activityRepository;

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
    private StaffRepository staffRepository;


    @Autowired
    private EmailService emailService;

    @Override
    public Object register(RegisterRequest registerRequest) {
        List<String> staffEmails = Arrays.asList(
                "amoghbajracharya@gmail.com"
        );
        emailService.sendRegistrationApprovalRequest(staffEmails, registerRequest);
        if (Objects.equals(registerRequest.getRole(), "User")){
            User user = new User();
            user.setUserEmail(registerRequest.getUserEmail());
            user.setUserName(registerRequest.getUserName());
            user.setUserPassword(encoder.encode(registerRequest.getUserPassword()));
            user.setStatus(false);
            user.setRegistrationDate(LocalDate.now());
            user.setRole(registerRequest.getRole());
            userRepository.save(user);
            Activity activity = new Activity();
            activity.setActivityDescription(registerRequest.getUserEmail() + "requested for Registration");
            activity.setActivityTime(LocalDateTime.now());
            activity.setUser(user);
            return setResponseFields(null,200,"Registration request sent to staff for approval!",true);
        }
        else{
            Staff user = new Staff();
            user.setStaffEmail(registerRequest.getUserEmail());
            user.setStaffName(registerRequest.getUserName());
            user.setStaffPassword(encoder.encode(registerRequest.getUserPassword()));
            user.setStatus(false);
            user.setRegistrationDate(LocalDate.now());
            user.setRole(registerRequest.getRole());
            staffRepository.save(user);
            Activity activity = new Activity();
            activity.setActivityDescription(registerRequest.getUserEmail() + "requested for Registration");
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(user);
            return setResponseFields(null,200,"Registration request sent to staff for approval!",true);
        }

    }

    @Override
    public Object pendingUserList() {

        List<Object> userList = new ArrayList<>(userRepository.findByStatusFalse());
        List<Object> staffList = new ArrayList<>(staffRepository.findByStatusFalse());

        userList.addAll(staffList);

        return setResponseFields(userList, 200, "Pending User List Generated", true);
    }


    @Override
    public Object approveRequest(ApproveRequest approveRequest) {
        Activity activity = new Activity();
        User userRequest = userRepository.findByUserEmail(approveRequest.getTargetUserEmail());
        if (userRequest != null) {
            if (userRequest.getStatus()) {
                return setResponseFields(null, 404, "User Already Registered", false);
            }
            userRequest.setApproveDate(LocalDate.now());
            userRequest.setStatus(true);
            userRepository.save(userRequest);
            activity.setActivityDescription(approveRequest.getPerformedByEmail() + "Registered" +approveRequest.getTargetUserEmail());
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(staffRepository.findByStaffEmail(approveRequest.getPerformedByEmail()));
            activityRepository.save(activity);
            return setResponseFields(null, 200, "User Registered", true);
        }

        Staff staffRequest = staffRepository.findByStaffEmail(approveRequest.getTargetUserEmail());
        if (staffRequest != null) {
            if (staffRequest.getStatus()) {
                return setResponseFields(null, 404, "Staff Already Registered", false);
            }
            staffRequest.setApproveDate(LocalDate.now());
            staffRequest.setStatus(true);
            staffRepository.save(staffRequest);
            activity.setActivityDescription(approveRequest.getPerformedByEmail() + "Registered" +approveRequest.getTargetUserEmail());
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(staffRepository.findByStaffEmail(approveRequest.getPerformedByEmail()));
            activityRepository.save(activity);
            return setResponseFields(null, 200, "Staff Registered", true);
        }

        return setResponseFields(null, 404, "User or Staff Not Found", false);
    }

    @Override
    public Object rejectRequest(RejectRequest rejectRequest) {
        Activity activity = new Activity();
        User userRequest = userRepository.findByUserEmail(rejectRequest.getTargetUserEmail());
        if (userRequest != null) {
            if (userRequest.getStatus()) {
                return setResponseFields(null, 400, "User Already Registered", false);
            }
            userRepository.delete(userRequest);
            activity.setActivityDescription(rejectRequest.getPerformedByEmail() + "rejected" +rejectRequest.getTargetUserEmail());
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(staffRepository.findByStaffEmail(rejectRequest.getPerformedByEmail()));
            activityRepository.save(activity);
            return setResponseFields(null, 200, "User registration request rejected.", true);
        }

        Staff staffRequest = staffRepository.findByStaffEmail(rejectRequest.getTargetUserEmail());
        if (staffRequest != null) {
            if (staffRequest.getStatus()) {

                return setResponseFields(null, 400, "Staff Already Registered", false);
            }
            staffRepository.delete(staffRequest);
            activity.setActivityDescription(rejectRequest.getPerformedByEmail() + "rejected" +rejectRequest.getTargetUserEmail());
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(staffRepository.findByStaffEmail(rejectRequest.getPerformedByEmail()));
            activityRepository.save(activity);
            return setResponseFields(null, 200, "Staff registration request rejected.", true);
        }

        return setResponseFields(null, 404, "User or Staff Not Found", false);
    }

}
