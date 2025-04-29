package com.lambdacode.creditscoreanalysiswebapplication.Service.MyUserDetailsServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Staff;
import com.lambdacode.creditscoreanalysiswebapplication.Model.StaffPrinciple;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Model.UserPrinciple;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.StaffRepository;
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
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        User user = userRepository.findByUserEmail(email);
        Staff staff = staffRepository.findByStaffEmail(email);
        if (user != null && user.getStatus()) {
            return new UserPrinciple(user);
        } else if (staff != null && staff.getStatus()) {
            return new StaffPrinciple(staff);
        }

        throw new UsernameNotFoundException("Authentication failed.");
    }
}

