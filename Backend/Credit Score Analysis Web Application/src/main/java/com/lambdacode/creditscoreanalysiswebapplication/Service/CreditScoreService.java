package com.lambdacode.creditscoreanalysiswebapplication.Service;

import ai.onnxruntime.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdacode.creditscoreanalysiswebapplication.Request.CreditDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CreditScoreService {
    @Autowired
    DataPreProcessingService preprocessingService;

    private OrtEnvironment env;
    private OrtSession session;

    public CreditScoreService() throws OrtException, IOException {
        env = OrtEnvironment.getEnvironment();
        String modelPath = getModelPath();
        session = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    private String getModelPath() throws IOException {
        // Load model from resources/Model/credit_score_model.onnx
        Path modelFile = Files.createTempFile("credit_score_model", ".onnx");
        Files.copy(
                getClass().getClassLoader().getResourceAsStream("Model/credit_score_model.onnx"),
                modelFile,
                StandardCopyOption.REPLACE_EXISTING
        );
        return modelFile.toString();
    }
    public float[] predict(CreditDataRequest creditData) throws OrtException, IOException {
        System.out.println("Credit Details List: " + creditData.getCreditUtilizationRatio());
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Model/MinMax.json");
        if (inputStream == null) {
            throw new IOException("Cannot find resource 'Model/MinMax.json'");
        }
        // Parse the JSON file from the InputStream
        JsonNode rootNode = mapper.readTree(inputStream);
        inputStream.close();
        float[] creditDetails = new float[]{
                (float) preprocessingService.normalize(creditData.getCreditUtilizationRatio(),
                        rootNode.path("Credit_Utilization_Ratio").path("min").asDouble(),
                        rootNode.path("Credit_Utilization_Ratio").path("max").asDouble()),
                (float) preprocessingService.normalize(creditData.getNumOfDelayedPayment(),
                        rootNode.path("Num_of_Delayed_Payment").path("min").asDouble(),
                        rootNode.path("Num_of_Delayed_Payment").path("max").asDouble()),
                (float) preprocessingService.normalize(creditData.getCreditHistoryAgeMonths(),
                        rootNode.path("Credit_History_Age_Months").path("min").asDouble(),
                        rootNode.path("Credit_History_Age_Months").path("max").asDouble()),
                (float) preprocessingService.normalize(creditData.getNumCreditInquiries(),
                        rootNode.path("Num_Credit_Inquiries").path("min").asDouble(),
                        rootNode.path("Num_Credit_Inquiries").path("max").asDouble()),
                (float) preprocessingService.normalize(creditData.getCreditMix(),
                        rootNode.path("Credit_Mix_Number").path("min").asDouble(),
                        rootNode.path("Credit_Mix_Number").path("max").asDouble())
        };

        // Optionally, print the list to verify contents
        System.out.println("Credit Details List: " + creditDetails);

        System.out.println("Received input data: " + Arrays.toString(creditDetails));

        // Ensure creditDetails has exactly 4 elements
        if (creditDetails.length != 5) {
            throw new IllegalArgumentException("❌ Invalid input size: Expected 5 values, but received " + creditDetails.length);
        }

        long[] shape = {1, 5};  // Ensure shape matches the ONNX model

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

            // 🔥 FIX: Convert `float[][]` to `float[]`
            float[][] outputArray = (float[][]) result.get(0).getValue();
            float[] output = outputArray[0]; // Extract 1D array

            System.out.println("✅ Model output: " + Arrays.toString(output));

            return output;

        } catch (OrtException e) {
            System.err.println("❌ Error while running ONNX model: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

