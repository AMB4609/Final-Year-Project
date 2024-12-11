package com.lambdacode.creditscoreanalysis.Controller;


import com.lambdacode.creditscoreanalysis.Request.LoginRequest;
import com.lambdacode.creditscoreanalysis.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @GetMapping("/loginUser")
    public String verifyLogin(LoginRequest loginRequest) {
        return loginService.verify(loginRequest);
    }
}
