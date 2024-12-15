package com.lambdacode.creditscoreanalysiswebapplication.Controller;

import com.lambdacode.creditscoreanalysiswebapplication.Request.LoginRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/loginUser")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Object login = loginService.login(loginRequest);
        return ResponseEntity.ok(login);
    }
}
