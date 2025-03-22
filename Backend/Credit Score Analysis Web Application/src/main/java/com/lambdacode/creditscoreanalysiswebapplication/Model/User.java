package com.lambdacode.creditscoreanalysiswebapplication.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Integer userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String role;
    private boolean status;
    private LocalDate approveDate;
    private LocalDate registrationDate;

    // Getter and Setter for userId
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // Getter and Setter for userEmail
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // Getter and Setter for userPassword
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    // Getter and Setter for userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for status
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Getter and Setter for approveDate
    public LocalDate getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(LocalDate approveDate) {
        this.approveDate = approveDate;
    }

    // Getter and Setter for registrationDate
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
