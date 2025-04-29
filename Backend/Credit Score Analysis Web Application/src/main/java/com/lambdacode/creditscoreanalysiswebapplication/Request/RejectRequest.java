package com.lambdacode.creditscoreanalysiswebapplication.Request;

public class RejectRequest {

    private String targetUserEmail; // Email of user being rejected
    private String performedByEmail; // Email of the user performing the rejection

    public RejectRequest() {
    }

    public RejectRequest(String targetUserEmail, String performedByEmail) {
        this.targetUserEmail = targetUserEmail;
        this.performedByEmail = performedByEmail;
    }

    public String getTargetUserEmail() {
        return targetUserEmail;
    }

    public void setTargetUserEmail(String targetUserEmail) {
        this.targetUserEmail = targetUserEmail;
    }

    public String getPerformedByEmail() {
        return performedByEmail;
    }

    public void setPerformedByEmail(String performedByEmail) {
        this.performedByEmail = performedByEmail;
    }
}
