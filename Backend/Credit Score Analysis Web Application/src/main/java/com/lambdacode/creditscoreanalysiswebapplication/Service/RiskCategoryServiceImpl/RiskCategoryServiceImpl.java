package com.lambdacode.creditscoreanalysiswebapplication.Service.RiskCategoryServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.PredictionLog;

import com.lambdacode.creditscoreanalysiswebapplication.Repository.PredictionLogRepository;

import com.lambdacode.creditscoreanalysiswebapplication.Service.RiskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskCategoryServiceImpl implements RiskCategoryService {



    @Autowired
    PredictionLogRepository predictionLogRepository;


    @Override
    public String riskCategoryCalculation(Integer userId,float currentCreditScore) {

        List<PredictionLog> history = predictionLogRepository.findByUser_UserId(userId);

        if (history == null || history.isEmpty()) {

            return basicRisk(currentCreditScore);
        }

        List<Float> pastScores = history.stream()
                .map(PredictionLog::getCreditScore)
                .toList();

        double pastAvg = pastScores.stream().mapToDouble(Float::doubleValue).average().orElse(0);
        float lastPastScore = pastScores.get(pastScores.size() - 1);

        float changeFromAvg = currentCreditScore - (float) pastAvg;
        float changeFromLast = currentCreditScore - lastPastScore;

        if (changeFromAvg > 50 && changeFromLast > 20) {
            return "LowRisk";
        } else if (changeFromAvg >= -20 && changeFromLast >= -10) {
            return "MediumRisk";
        } else {
            return "HighRisk";
        }
    }

    private String basicRisk(float score) {
        if (score >= 750) return "LowRisk Basic";
        else if (score >= 500) return "MediumRisk Basic";
        else return "HighRisk Basic";
    }

}
