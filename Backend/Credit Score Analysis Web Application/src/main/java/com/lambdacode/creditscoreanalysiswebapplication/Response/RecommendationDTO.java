package com.lambdacode.creditscoreanalysiswebapplication.Response;

public class RecommendationDTO {
    private String recommendationText;

    public RecommendationDTO() {}

    public RecommendationDTO(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    // Getter
    public String getRecommendationText() {
        return recommendationText;
    }

    // Setter
    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }
}
