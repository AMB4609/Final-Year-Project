package com.lambdacode.creditscoreanalysiswebapplication.Service;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Recommendation;
import com.lambdacode.creditscoreanalysiswebapplication.Request.CreditDataRequest;

import java.util.List;

public interface RecommendationService {
    List<Recommendation> generateRecommendations(CreditDataRequest input, float creditScore);
}
