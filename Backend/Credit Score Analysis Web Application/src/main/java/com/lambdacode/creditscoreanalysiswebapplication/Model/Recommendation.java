package com.lambdacode.creditscoreanalysiswebapplication.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prediction_log_id", nullable = true)
    private PredictionLog predictionLog;

    private String recommendationText;

    // Constructor
    public Recommendation(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public Recommendation() {

    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public void setPredictionLog(PredictionLog predictionLog) {
        this.predictionLog = predictionLog;
    }
}
