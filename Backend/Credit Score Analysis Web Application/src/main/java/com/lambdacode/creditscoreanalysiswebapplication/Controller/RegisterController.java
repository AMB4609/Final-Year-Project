package com.lambdacode.creditscoreanalysiswebapplication.Controller;


import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.ApproveRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterRequest registerRequest) {
       Object RegisterResponse =  registerService.register(registerRequest);
        return null;
    }
    @GetMapping("/pendingApprovalUserList")
    public ResponseEntity<Object> getPendingApprovalUserList() {
        Object Userlist = registerService.pendingUserList();
        return ResponseEntity.ok(Userlist);
    }
    @GetMapping("/approve")
    public String approveUserRegistration(@RequestBody ApproveRequest approveRequest) {
        // Logic to fetch user details (Optional: Store pending users before approval)
        User registerRequest = userRepository.findByUserEmail(approveRequest.getEmail());
        if (registerRequest == null && registerRequest.getStatus() == true) {
            return "User not found or already registered.";
        }
        registerRequest.setApproveDate(LocalDate.now());
        registerRequest.setStatus(true);

        userRepository.save(registerRequest);

        return "User registration approved and user has been registered.";
    }
    @GetMapping("/reject")
    public String rejectUserRegistration(@RequestParam String email) {
        // Logic to remove or mark the request as rejected
        return "User registration request rejected.";
    }
}
