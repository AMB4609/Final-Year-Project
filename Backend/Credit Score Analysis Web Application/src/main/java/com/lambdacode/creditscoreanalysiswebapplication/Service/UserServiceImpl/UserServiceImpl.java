package com.lambdacode.creditscoreanalysiswebapplication.Service.UserServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Model.PredictionLog;
import com.lambdacode.creditscoreanalysiswebapplication.Model.Staff;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.*;
import com.lambdacode.creditscoreanalysiswebapplication.Request.EditRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.UserIdRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Response.UserProfileResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Base64;

import java.io.IOException;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;

    private JwtResponse setResponseFields(Object data, int code, String message, boolean status) {
        JwtResponse dto = new JwtResponse();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setStatus(status);
        dto.setData(data);
        return dto;
    }

    Activity activity = new Activity();
    private String convertToBase64(byte[] data) {
        return (data != null) ? Base64.getEncoder().encodeToString(data) : null;
    }
    private UserProfileResponse mapUserToDTO(User user) {
        UserProfileResponse dto = new UserProfileResponse();
        dto.setUserId(Long.valueOf(user.getUserId()));
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setApproveDate(user.getApproveDate());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setProfilePictureBase64(convertToBase64(user.getProfilePicture()));
        return dto;
    }

    private UserProfileResponse mapStaffToDTO(Staff staff) {
        UserProfileResponse dto = new UserProfileResponse();
        dto.setUserId(staff.getUserId());
        dto.setUserName(staff.getStaffName());
        dto.setUserEmail(staff.getStaffEmail());
        dto.setRole(staff.getRole());
        dto.setStatus(staff.getStatus());
        dto.setApproveDate(staff.getApproveDate());
        dto.setRegistrationDate(staff.getRegistrationDate());
        dto.setStaffPhone(staff.getStaffPhone());
        dto.setStaffAddress(staff.getStaffAddress());
        dto.setProfilePictureBase64(convertToBase64(staff.getProfilePicture()));
        return dto;
    }

    @Override
    public Object deleteUser(String UserEmail,String PerformedByEmail) {
        Staff staff = staffRepository.findByStaffEmail(UserEmail);
        Staff staffperformed = staffRepository.findByStaffEmail(PerformedByEmail);
       User user =  userRepository.findByUserEmail(UserEmail);
        if (user != null) {
            userRepository.deleteById(user.getUserId());
            activity.setActivityDescription("Deleted"+UserEmail+"by"+PerformedByEmail);
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(staffperformed);
            activityRepository.save(activity);
            return setResponseFields(null, 200, "Deleted User Successfully", true);

        }
        if (staff != null){
            staffRepository.deleteById(Math.toIntExact(staff.getUserId()));
            activity.setActivityDescription("Deleted"+UserEmail+"by"+PerformedByEmail);
            activity.setActivityTime(LocalDateTime.now());
            activity.setStaff(staffperformed);
            activityRepository.save(activity);
            return setResponseFields(null, 200, "Deleted Staff Successfully", true);
        }
        return setResponseFields(null,404,"User Not Found",false);
    }
    @Override
    public Object getUserByEmail(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        Staff staff = staffRepository.findByStaffEmail(userEmail);


        if (user != null) {
            UserProfileResponse userDTO =  mapUserToDTO(user);
            return setResponseFields(userDTO, 200, "Fetched User Successfully", true);
        } else if (staff != null) {
            UserProfileResponse staffDTO =  mapStaffToDTO(staff);
            return setResponseFields(staffDTO, 200, "Fetched User Successfully", true);
        } else {
            return setResponseFields(null, 404, "User Not Found", false);
        }
    }


    @Override
    public Object editUser(EditRequest editRequest) throws IOException {
        User user = userRepository.findByUserEmail(editRequest.getUserEmail());
        Staff staff = staffRepository.findByStaffEmail(editRequest.getUserEmail());

        if (user != null) {

            if (editRequest.getUserName() != null) user.setUserName(editRequest.getUserName());
            if (editRequest.getProfilePicture() != null) {
                MultipartFile profilePicture = editRequest.getProfilePicture();
                System.out.println("Image Size (bytes): " + profilePicture.getSize());
                System.out.println("Image Original Filename: " + profilePicture.getOriginalFilename());
                user.setProfilePicture(profilePicture.getBytes());
            }
            userRepository.save(user);
            UserProfileResponse userDTO =  mapUserToDTO(user);
            if (user.getProfilePicture() != null) {
                System.out.println("Saved Image Length: " + user.getProfilePicture().length);
            } else {
                System.out.println("Saved profile picture is NULL.");
            }

            return setResponseFields(userDTO, 200, "User Updated Successfully", true);
        } else if (staff != null) {

            if (editRequest.getUserName() != null) staff.setStaffName(editRequest.getUserName());
            if (editRequest.getStaffAddress() != null) staff.setStaffAddress(editRequest.getStaffAddress());
            if (editRequest.getStaffPhone() != null) staff.setStaffPhone(editRequest.getStaffPhone());
            if (editRequest.getProfilePicture() != null) {

                MultipartFile profilePicture = editRequest.getProfilePicture();
                staff.setProfilePicture(profilePicture.getBytes());

            }
            staffRepository.save(staff);
            UserProfileResponse staffDTO =  mapStaffToDTO(staff);
            return setResponseFields(staffDTO, 200, "Staff Updated Successfully", true);
        }
        return setResponseFields(null, 404, "User/Staff Not Found", false);
    }

    @Override
    public Object getAllUsers(int page, int size) {
        List<User> users = userRepository.findAll(PageRequest.of(page, size)).getContent();
        if (users.isEmpty()) {
            return setResponseFields(null, 404, "No Users Found", false);
        }
        return setResponseFields(users, 200, "Fetched Users Successfully", true);
    }


    @Override
    public Object getAllStaff(int page, int size) {
        List<Staff> staff = staffRepository.findAll(PageRequest.of(page, size)).getContent();
        if (staff.isEmpty()) {
            return setResponseFields(null, 404, "No Staff Found", false);
        }
        return setResponseFields(staff, 200, "Fetched Staff Successfully", true);
    }
    @Override
    public Object changeUserRole(String userEmail, String newRole) throws IOException {
        User user = userRepository.findByUserEmail(userEmail);
        Staff staff = staffRepository.findByStaffEmail(userEmail);

        if (user != null) {

            if (newRole.equalsIgnoreCase("STAFF") || newRole.equalsIgnoreCase("ADMIN")) {
                activityRepository.deleteActivitiesByUser(user);
                Staff newStaff = new Staff();
                newStaff.setStaffEmail(user.getUserEmail());
                newStaff.setStaffName(user.getUserName());
                newStaff.setRole(newRole);
                newStaff.setProfilePicture(user.getProfilePicture());
                newStaff.setStatus(user.getStatus());
                newStaff.setRegistrationDate(user.getRegistrationDate());
                newStaff.setApproveDate(user.getApproveDate());

                staffRepository.save(newStaff);
                userRepository.delete(user);

                return setResponseFields(null, 200, "User shifted to " + newRole + " successfully", true);
            } else {
                return setResponseFields(null, 400, "Invalid role transition", false);
            }
        } else if (staff != null) {
            if (newRole.equalsIgnoreCase("USER")) {
                activityRepository.deleteActivitiesByStaff(staff);
                User newUser = new User();
                newUser.setUserEmail(staff.getStaffEmail());
                newUser.setUserName(staff.getStaffName());
                newUser.setRole(newRole);
                newUser.setProfilePicture(staff.getProfilePicture());
                newUser.setStatus(staff.getStatus());
                newUser.setRegistrationDate(staff.getRegistrationDate());
                newUser.setApproveDate(staff.getApproveDate());

                userRepository.save(newUser);
                staffRepository.delete(staff);

                return setResponseFields(null, 200, "Staff/Admin shifted to User successfully", true);
            } else {
                return setResponseFields(null, 400, "Invalid role transition", false);
            }
        }

        return setResponseFields(null, 404, "User/Staff not found", false);
    }

}
