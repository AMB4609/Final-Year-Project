package com.lambdacode.creditscoreanalysiswebapplication.Request;

public class AppendRecommendationRequest {

    private String recommendationNotes;
    private String userEmail;

    public AppendRecommendationRequest() {
    }

    public AppendRecommendationRequest(String recommendationNotes, String userEmail) {
        this.recommendationNotes = recommendationNotes;
        this.userEmail = userEmail;
    }

    public String getRecommendationNotes() {
        return recommendationNotes;
    }

    public void setRecommendationNotes(String recommendationNotes) {
        this.recommendationNotes = recommendationNotes;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
