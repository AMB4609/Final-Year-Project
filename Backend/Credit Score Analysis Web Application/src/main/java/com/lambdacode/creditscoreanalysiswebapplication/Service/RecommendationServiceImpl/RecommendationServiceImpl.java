package com.lambdacode.creditscoreanalysiswebapplication.Service.RecommendationServiceImpl;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Recommendation;
import com.lambdacode.creditscoreanalysiswebapplication.Request.CreditDataRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public List<Recommendation> generateRecommendations(CreditDataRequest input, float creditScore) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (input.getCreditUtilizationRatio() > 70) {
            recommendations.add(new Recommendation("Your credit utilization is high. Try to keep it below 30% to improve your score."));
        } else if (input.getCreditUtilizationRatio() < 30) {
            recommendations.add(new Recommendation("You're using very little credit. A small, healthy utilization can help build trust."));
        }

        if (input.getNumOfDelayedPayment() > 3) {
            recommendations.add(new Recommendation("You've had several delayed payments. Pay dues on time to build a strong credit history."));
        } else if (input.getNumOfDelayedPayment() == 0) {
            recommendations.add(new Recommendation("Excellent! No delayed payments — keep it up."));
        }

        if (input.getCreditHistoryAgeMonths() < 12) {
            recommendations.add(new Recommendation("Your credit history is short. Keeping accounts open longer helps your score."));
        } else if (input.getCreditHistoryAgeMonths() >= 36) {
            recommendations.add(new Recommendation("Good job maintaining a long credit history."));
        }

        if (input.getNumCreditInquiries() > 4) {
            recommendations.add(new Recommendation("Too many recent credit inquiries can be risky. Only apply for credit when needed."));
        } else if (input.getNumCreditInquiries() == 0) {
            recommendations.add(new Recommendation("No recent inquiries — that's good for stability."));
        }

        if (input.getCreditMix() < 1) {
            recommendations.add(new Recommendation("Consider diversifying your credit types (e.g., credit card + loan) to improve your score."));
        } else if (input.getCreditMix() >= 2) {
            recommendations.add(new Recommendation("Your credit mix is strong — great for credit health."));
        }

        if (creditScore < 600) {
            recommendations.add(new Recommendation("Consider secured credit cards or small credit lines to build trust."));
        }

        if (recommendations.isEmpty()) {
            recommendations.add(new Recommendation("You're on the right track! Keep managing your credit responsibly."));
        }

        return recommendations;
    }
}

