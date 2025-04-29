package com.lambdacode.creditscoreanalysiswebapplication.Response;

import java.util.List;

public class PredictionHistoryResponse {
    private Integer id;
    private List<RecommendationDTO> recommendations;
    private float creditScore;
    private String riskCategory;
    private String predictionDate;
    private Boolean needRecommendation;

    // No-args constructor
    public PredictionHistoryResponse() {}

    // All-args constructor
    public PredictionHistoryResponse(Integer id, List<RecommendationDTO> recommendations, float creditScore,
                                     String riskCategory, String predictionDate,
                                      Boolean needRecommendation) {
        this.id = id;
        this.recommendations = recommendations;
        this.creditScore = creditScore;
        this.riskCategory = riskCategory;
        this.predictionDate = predictionDate;

        this.needRecommendation = needRecommendation;
    }

    // Getter and Setter methods

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }

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

    public String getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(String predictionDate) {
        this.predictionDate = predictionDate;
    }


    public Boolean getNeedRecommendation() {
        return needRecommendation;
    }

    public void setNeedRecommendation(Boolean needRecommendation) {
        this.needRecommendation = needRecommendation;
    }
}
