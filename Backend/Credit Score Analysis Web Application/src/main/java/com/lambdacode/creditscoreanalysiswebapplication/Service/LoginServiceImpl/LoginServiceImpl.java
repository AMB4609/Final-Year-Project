package com.lambdacode.creditscoreanalysiswebapplication.Service.LoginServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.StaffPrinciple;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Model.UserPrinciple;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.LoginRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Service.JWTService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.LoginService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.MyUserDetailsServiceImpl.MyUserDetailsServiceImpl;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {
    private JwtResponse setResponseFields(Object data, int code, String message, boolean status) {
        JwtResponse dto = new JwtResponse();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setStatus(status);
        dto.setData(data);
        return dto;
    }
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsServiceImpl;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Object login(LoginRequest loginRequest) {

        UserDetails userDetails = myUserDetailsServiceImpl.loadUserByUsername(loginRequest.getUserEmail());


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getUserPassword()));
        if (authentication.isAuthenticated()) {
            String entityType = null;
            String role = null;
            if (userDetails instanceof UserPrinciple) {
                entityType = "USER";
                role = "USER";

            } else if (userDetails instanceof StaffPrinciple) {
                entityType = "STAFF";
                role = ((StaffPrinciple) userDetails).getPosition().toUpperCase();
            } else {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }


            System.out.println("Entity Type: " + entityType);
            System.out.println("Role: " + role);
            String token = jwtService.generateToken(loginRequest.getUserEmail(), entityType, role);

            return setResponseFields(token,200,"login successful",true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }
}