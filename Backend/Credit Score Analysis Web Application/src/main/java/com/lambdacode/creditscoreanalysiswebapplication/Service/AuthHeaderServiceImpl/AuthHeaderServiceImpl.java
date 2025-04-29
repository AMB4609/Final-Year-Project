package com.lambdacode.creditscoreanalysiswebapplication.Service.AuthHeaderServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Service.AuthHeaderService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthHeaderServiceImpl implements AuthHeaderService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JWTService jwtService;
    @Override
    public User getUserId(String authHeader) {
        String token = authHeader.trim();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        String email = jwtService.extractEmail(token);
        return userRepository.findByUserEmail(email);
    }
}
