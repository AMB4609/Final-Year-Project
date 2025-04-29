package com.lambdacode.creditscoreanalysiswebapplication.Request;

public class RoleChangeRequest {
    private String userEmail;
    private String newRole;

    // Getters and Setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}
