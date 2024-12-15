package com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.RegisterRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;


@Service
public class RegisterServiceImpl implements RegisterService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserRepository userRepository;


    @Override
    public void register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUserEmail(registerRequest.getUserEmail());
        user.setUserPassword(encoder.encode(registerRequest.getUserPassword()));
        user.setUserName(registerRequest.getUserName());
        user.setUserPhone(registerRequest.getUserPhone());
        userRepository.save(user);
    }
}
