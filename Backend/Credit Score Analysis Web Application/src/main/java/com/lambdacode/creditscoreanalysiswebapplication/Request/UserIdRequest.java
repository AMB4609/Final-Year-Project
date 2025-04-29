package com.lambdacode.creditscoreanalysiswebapplication.Request;

public class UserIdRequest {

    private String userEmail;
    private int staffId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userId) {
        this.userEmail = userId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
