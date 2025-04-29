package com.lambdacode.creditscoreanalysiswebapplication.Service;


import org.springframework.stereotype.Service;

@Service
public class DataPreProcessingService {

    public static double normalize(double value, double min, double max) {
        if (max == min) {
            return 0;
        }
        return (value - min) / (max - min);
    }
}
