package com.lambdacode.creditscoreanalysiswebapplication.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

import java.util.List;

@Entity(name = "predictionLog")
public class PredictionLog {
    @Id
    @GeneratedValue
    private Integer id;
    private float CreditScore;
    private String RiskCategory;
    @OneToMany(mappedBy = "predictionLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Recommendation> recommendations;
    private LocalDate PredictionDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;;
    private boolean needRecommendation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getCreditScore() {
        return CreditScore;
    }

    public void setCreditScore(float creditScore) {
        CreditScore = creditScore;
    }

    public String getRiskCategory() {
        return RiskCategory;
    }

    public void setRiskCategory(String riskCategory) {
        RiskCategory = riskCategory;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public LocalDate getPredictionDate() {
        return PredictionDate;
    }

    public void setPredictionDate(LocalDate predictionDate) {
        PredictionDate = predictionDate;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNeedRecommendation() {
        return needRecommendation;
    }

    public void setNeedRecommendation(boolean needRecommendation) {
        this.needRecommendation = needRecommendation;
    }

}
