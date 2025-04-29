package com.lambdacode.creditscoreanalysiswebapplication.Response;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Recommendation;

import java.util.List;

public class CreditResponse {
    private float creditScore;
    private String riskCategory;
    private List<Recommendation> recommendation;

    public float getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(float creditScore) {
        this.creditScore = creditScore;
    }

    public String getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(String riskCategory) {
        this.riskCategory = riskCategory;
    }

    public List<Recommendation> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<Recommendation> recommendation) {
        this.recommendation = recommendation;
    }
}
