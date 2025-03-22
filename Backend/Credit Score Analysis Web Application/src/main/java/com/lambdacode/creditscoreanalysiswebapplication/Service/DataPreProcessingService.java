package com.lambdacode.creditscoreanalysiswebapplication.Service;


import org.springframework.stereotype.Service;

@Service
public class DataPreProcessingService {
    /**
     * Normalizes a value using the specified min and max.
     * @param value The value to normalize.
     * @param min The minimum value used for normalization.
     * @param max The maximum value used for normalization.
     * @return Normalized value.
     */
    public static double normalize(double value, double min, double max) {
        if (max == min) {
            return 0; // Avoid division by zero and handle case where all values are the same.
        }
        return (value - min) / (max - min);
    }
}
