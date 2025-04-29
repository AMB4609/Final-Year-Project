package com.lambdacode.creditscoreanalysiswebapplication.Repository;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
}
