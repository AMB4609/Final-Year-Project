package com.lambdacode.creditscoreanalysiswebapplication.Controller;

import com.lambdacode.creditscoreanalysiswebapplication.Constant.AuthorizeConstant;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import com.lambdacode.creditscoreanalysiswebapplication.Request.CreditDataRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.AuthHeaderService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.CreditScoreService;
import com.lambdacode.creditscoreanalysiswebapplication.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import ai.onnxruntime.OrtException;
import org.springframework.web.bind.annotation.RestController;

import static com.lambdacode.creditscoreanalysiswebapplication.Constant.AuthorizeConstant.*;

@RestController
@RequestMapping("api/credit-score")
public class CreditScoreController {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private CreditScoreService creditScoreService;

    @Autowired
    AuthHeaderService authHeaderService;


    @PostMapping("/predict")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> predict( @RequestHeader("Authorization") String authHeader,@RequestBody CreditDataRequest input) throws OrtException, IOException {
        User id = authHeaderService.getUserId(authHeader);
        Object output = creditScoreService.predict(id,input);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/history/{UserEmail}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAllPrediction( @PathVariable String UserEmail) throws OrtException, IOException {
        return ResponseEntity.ok(creditScoreService.getPredictionHistory(UserEmail));
    }
}
