package com.lambdacode.creditscoreanalysiswebapplication.Request;

public class ApproveRequest {

    private String targetUserEmail;
    private String performedByEmail;

    public ApproveRequest() {
    }

    public ApproveRequest(String targetUserEmail, String performedByEmail) {
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
