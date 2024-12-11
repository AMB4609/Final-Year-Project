//package com.lambdacode.creditscoreanalysis.Controller;
//
//import com.lambdacode.creditscoreanalysis.Request.RegisterRequest;
//import com.lambdacode.creditscoreanalysis.Service.RegisterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/register")
//public class RegisterController {
//    @Autowired
//    private RegisterService registerService;
//    @PostMapping("registerUser")
//    public String registerUser(RegisterRequest registerRequest) {
//        registerService.registerUser(registerRequest);
//        return "registered Successfully";
//    }
//}
