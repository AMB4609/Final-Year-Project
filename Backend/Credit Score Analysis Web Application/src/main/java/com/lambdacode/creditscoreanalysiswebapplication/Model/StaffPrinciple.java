package com.lambdacode.creditscoreanalysiswebapplication.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class StaffPrinciple implements UserDetails {
    private final Staff staff;
    public StaffPrinciple(Staff staff) {
        this.staff = staff;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + staff.getRole().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return staff.getStaffPassword();
    }

    @Override
    public String getUsername() {
        return staff.getStaffEmail();
    }
    public String getPosition(){
        return staff.getRole();
    }
}
