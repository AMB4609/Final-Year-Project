package com.lambdacode.creditscoreanalysiswebapplication.Service.LoginServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Model.UserPrinciple;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.LoginRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.JWTService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.LoginService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.MyUserDetailsServiceImpl.MyUserDetailsServiceImpl;
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
        // Load UserDetails by email (username)
        UserDetails userDetails = myUserDetailsServiceImpl.loadUserByUsername(loginRequest.getUserEmail());

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getUserPassword()));
        if (authentication.isAuthenticated()) {
            String entityType = null; // Initialize entityType to null
            String role = null;       // Initialize role to null
            // Determine if the user is a `staff` or a `user`
            if (userDetails instanceof UserPrinciple) {
                entityType = "user";
                // role remains null for users since it's not applicable here
            } else {
                // If neither, return unauthorized response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // Debug output to verify values of entityType and role
            System.out.println("Entity Type: " + entityType);
            System.out.println("Role: " + role);
            String token = jwtService.generateToken(loginRequest.getUserEmail(), entityType, role);
            // Generate and return the JWT token
            return token;
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }
}
