package com.lambdacode.creditscoreanalysiswebapplication.Controller;

import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;

import com.lambdacode.creditscoreanalysiswebapplication.Request.ApproveRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;

import com.lambdacode.creditscoreanalysiswebapplication.Request.RejectRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterRequest registerRequest) {
       Object RegisterResponse =  registerService.register(registerRequest);
        return ResponseEntity.ok(RegisterResponse);
    }
    @GetMapping("/pendingApprovalUserList")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Object> getPendingApprovalUserList() {
        Object Userlist = registerService.pendingUserList();
        return ResponseEntity.ok(Userlist);
    }
    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public Object approveUserRegistration(@RequestBody ApproveRequest approveRequest) {
        return registerService.approveRequest(approveRequest);
    }

    @PostMapping("/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public Object rejectUserRegistration(@RequestBody RejectRequest rejectRequest) {
        return registerService.rejectRequest(rejectRequest);
    }


}
