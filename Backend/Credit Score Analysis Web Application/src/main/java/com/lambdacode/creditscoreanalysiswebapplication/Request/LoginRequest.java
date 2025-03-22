package com.lambdacode.creditscoreanalysiswebapplication.Request;

public class LoginRequest {
    private String userEmail;
    private String userPassword;

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
}
