package com.lambdacode.creditscoreanalysiswebapplication.Service.MyUserDetailsServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Model.UserPrinciple;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find a staff or user entity by email.

        User user = userRepository.findByUserEmail(email);
        if (user != null) {
            return new UserPrinciple(user);
        }

        // If neither a staff nor a user is found, throw an exception.
        // To avoid giving specific details about which part of the authentication failed,
        // a generic error message is used.
        throw new UsernameNotFoundException("Authentication failed.");
    }
}

