package com.lambdacode.creditscoreanalysiswebapplication.Repository;

import com.lambdacode.creditscoreanalysiswebapplication.Model.PredictionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionLogRepository extends JpaRepository<PredictionLog, Integer> {
    List<PredictionLog> findByUser_UserId(Integer userId);

    List<PredictionLog> findByUserUserId(Integer userId);
}
