package com.lambdacode.creditscoreanalysiswebapplication.Controller;

import com.lambdacode.creditscoreanalysiswebapplication.Request.CreditDataRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.CreditScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import ai.onnxruntime.OrtException;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/credit-score")
public class CreditScoreController {

    @Autowired
    private CreditScoreService creditScoreService;

    @PostMapping("/predict")
    public ResponseEntity<float[]> predict(@RequestBody CreditDataRequest input) throws OrtException, IOException {
        float[] output = creditScoreService.predict(input);
        return ResponseEntity.ok(output);
    }
}
