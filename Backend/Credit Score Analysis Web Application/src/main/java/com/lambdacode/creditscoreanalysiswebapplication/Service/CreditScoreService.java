package com.lambdacode.creditscoreanalysiswebapplication.Service;

import ai.onnxruntime.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Model.PredictionLog;
import com.lambdacode.creditscoreanalysiswebapplication.Model.Recommendation;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.ActivityRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.PredictionLogRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.RecommendationRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Repository.UserRepository;
import com.lambdacode.creditscoreanalysiswebapplication.Request.CreditDataRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Response.CreditResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Response.JwtResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Response.PredictionHistoryResponse;
import com.lambdacode.creditscoreanalysiswebapplication.Response.RecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CreditScoreService {
    @Autowired
    DataPreProcessingService preprocessingService;

    @Autowired
    RiskCategoryService riskCategoryService;

    @Autowired
    PredictionLogRepository predictionLogRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecommendationService recommendationService;

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    ActivityRepository activityRepository;

    private JwtResponse setResponseFields(Object data, int code, String message, boolean status) {
        JwtResponse dto = new JwtResponse();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setStatus(status);
        dto.setData(data);
        return dto;
    }

    private OrtEnvironment env;
    private OrtSession session;

    public CreditScoreService() throws OrtException, IOException {
        env = OrtEnvironment.getEnvironment();
        String modelPath = getModelPath();
        session = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    private String getModelPath() throws IOException {

        Path modelFile = Files.createTempFile("credit_score_model", ".onnx");
        Files.copy(
                getClass().getClassLoader().getResourceAsStream("Model/credit_score_model_fixed.onnx"),
                modelFile,
                StandardCopyOption.REPLACE_EXISTING
        );
        return modelFile.toString();
    }
    public Object predict(User user ,CreditDataRequest creditData) throws OrtException, IOException {
        boolean minorCorrection = false;

        if (creditData.getCreditUtilizationRatio() < 0 || creditData.getCreditUtilizationRatio() > 120) {
            if (creditData.getCreditUtilizationRatio() > 1 && creditData.getCreditUtilizationRatio() <= 120) {
                creditData.setCreditUtilizationRatio(100);
                minorCorrection = true;
            } else {
                return setResponseFields(null, 400, "Invalid input: Credit Utilization Ratio must be between 0 and 100.", false);
            }
        }

        if (creditData.getNumOfDelayedPayment() < 0 || creditData.getNumOfDelayedPayment() > 5000) {
            if (creditData.getNumOfDelayedPayment() > 4399 && creditData.getNumOfDelayedPayment() <= 5000) {
                creditData.setNumOfDelayedPayment(4399);
                minorCorrection = true;
            } else {
                return setResponseFields(null, 400, "Invalid input: Number of Delayed Payments is unrealistic.", false);
            }
        }

        if (creditData.getCreditHistoryAgeMonths() < 0 || creditData.getCreditHistoryAgeMonths() > 500) {
            if (creditData.getCreditHistoryAgeMonths() > 408 && creditData.getCreditHistoryAgeMonths() <= 500) {
                creditData.setCreditHistoryAgeMonths(408);
                minorCorrection = true;
            } else {
                return setResponseFields(null, 400, "Invalid input: Credit History Age is unrealistic.", false);
            }
        }

        if (creditData.getNumCreditInquiries() < 0 || creditData.getNumCreditInquiries() > 3000) {
            if (creditData.getNumCreditInquiries() > 2597 && creditData.getNumCreditInquiries() <= 3000) {
                creditData.setNumCreditInquiries(2597);
                minorCorrection = true;
            } else {
                return setResponseFields(null, 400, "Invalid input: Number of Credit Inquiries is unrealistic.", false);
            }
        }

        if (creditData.getCreditMix() < 0 || creditData.getCreditMix() > 10) {
            return setResponseFields(null, 400, "Invalid input: Credit Mix Number must be between 0 and 10.", false);
        }
        if (minorCorrection) {
            Activity correctionActivity = new Activity();
            correctionActivity.setActivityDescription(" Slight input correction done for user " + user.getUserEmail() + ". Some fields were clipped to safe limits.");
            correctionActivity.setActivityTime(LocalDateTime.now());
            correctionActivity.setUser(user);
            activityRepository.save(correctionActivity);
        }
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Model/MinMax.json");
        if (inputStream == null) {
            throw new IOException("Cannot find resource 'Model/MinMax.json'");
        }

        JsonNode rootNode = mapper.readTree(inputStream);
        inputStream.close();
        float[] creditDetails = new float[]{
                1 - (float) preprocessingService.normalize(creditData.getNumOfDelayedPayment(),
                        rootNode.path("Num_of_Delayed_Payment").path("min").asDouble(),
                        rootNode.path("Num_of_Delayed_Payment").path("max").asDouble()),

                1 - (float) preprocessingService.normalize(creditData.getCreditUtilizationRatio(),
                        rootNode.path("Credit_Utilization_Ratio").path("min").asDouble(),
                        rootNode.path("Credit_Utilization_Ratio").path("max").asDouble()),

                (float) preprocessingService.normalize(creditData.getCreditHistoryAgeMonths(),
                        rootNode.path("Credit_History_Age_Months").path("min").asDouble(),
                        rootNode.path("Credit_History_Age_Months").path("max").asDouble()),

                1 - (float) preprocessingService.normalize(creditData.getNumCreditInquiries(),
                        rootNode.path("Num_Credit_Inquiries").path("min").asDouble(),
                        rootNode.path("Num_Credit_Inquiries").path("max").asDouble()),

                (float) preprocessingService.normalize(creditData.getCreditMix(),
                        rootNode.path("Credit_Mix_Number").path("min").asDouble(),
                        rootNode.path("Credit_Mix_Number").path("max").asDouble())
        };


        System.out.println("Credit Details List: " + creditDetails);

        System.out.println("Received input data: " + Arrays.toString(creditDetails));

        if (creditDetails.length != 5) {
            throw new IllegalArgumentException(" Invalid input size: Expected 5 values, but received " + creditDetails.length);
        }

        long[] shape = {1, 5};

        try {
            // ✅ Wrap creditDetails in a FloatBuffer
            FloatBuffer floatBuffer = FloatBuffer.wrap(creditDetails);
            System.out.println("✅ FloatBuffer created successfully");

            // ✅ Create ONNX tensor
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, floatBuffer, shape);
            System.out.println("✅ ONNX tensor created successfully");

            // ✅ Run the ONNX model
            OrtSession.Result result = session.run(Collections.singletonMap("float_input", inputTensor));
            System.out.println("✅ ONNX model ran successfully");

            float[][] outputArray = (float[][]) result.get(0).getValue();
            float[] output = outputArray[0];

            System.out.println("✅ Model output: " + Arrays.toString(output));
            CreditResponse creditResponse = new CreditResponse();
            String riskCategory = riskCategoryService.riskCategoryCalculation(user.getUserId(),output[0]);
            List<Recommendation> recommendation = recommendationService.generateRecommendations(creditData,output[0]);
            creditResponse.setCreditScore(output[0]);
            creditResponse.setRiskCategory(riskCategory);
            creditResponse.setRecommendation(recommendation);
            PredictionLog predictionLog = new PredictionLog();
            predictionLog.setCreditScore(output[0]);
            predictionLog.setRiskCategory(riskCategory);
            predictionLog.setPredictionDate(LocalDate.now());
            predictionLog.setRecommendations(recommendation);
            predictionLog.setNeedRecommendation(false);
            predictionLog.setUser(user);

            for (Recommendation rec : recommendation) {
                rec.setPredictionLog(predictionLog);
            }
            predictionLogRepository.save(predictionLog);
            recommendationRepository.saveAll(recommendation);
            Activity activity = new Activity();
            activity.setActivityDescription("Generated Credit score for " + user.getUserEmail() + " as " + predictionLog.getCreditScore());
            activity.setActivityTime(LocalDateTime.now());
            activity.setUser(predictionLog.getUser());
            activityRepository.save(activity);
            return setResponseFields(creditResponse,200,"Generated Credit Response",true);

        } catch (OrtException e) {
            System.err.println("❌ Error while running ONNX model: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Object getPredictionHistory(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        List<PredictionLog> history = predictionLogRepository.findByUser_UserId(user.getUserId());

        if (history.isEmpty()) {
            return setResponseFields(Collections.emptyList(), 200, "No prediction history found.", true);
        }

        List<PredictionHistoryResponse> responseList = history.stream().map(log -> {
            List<RecommendationDTO> recommendations = log.getRecommendations().stream()
                    .map(rec -> new RecommendationDTO(rec.getRecommendationText()))
                    .toList();

            return new PredictionHistoryResponse(
                    log.getId(),
                    recommendations,
                    log.getCreditScore(),
                    log.getRiskCategory(),
                    log.getPredictionDate().toString(),
                    log.isNeedRecommendation()
            );
        }).toList();

        return setResponseFields(responseList, 200, "Prediction history retrieved successfully.", true);
    }

}

