package com.lambdacode.creditscoreanalysis.Service.LoginServiceImpl;

import com.lambdacode.creditscoreanalysis.Model.User;
import com.lambdacode.creditscoreanalysis.Repository.UserRepository;
import com.lambdacode.creditscoreanalysis.Request.LoginRequest;
import com.lambdacode.creditscoreanalysis.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public String verify(LoginRequest loginRequest) {
        User user = userRepository.findByUserEmail(loginRequest.getUserEmail())
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        if(user.getUserEmail().equals(loginRequest.getUserEmail()) && user.getUserPassword().equals(loginRequest.getUserPassword())){
            return "User Logged in Successfully";
        }
    }
}
