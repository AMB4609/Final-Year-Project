package com.lambdacode.creditscoreanalysiswebapplication.Request;


import lombok.Data;

@Data
public class LoginRequest {
    private String userEmail;
    private String userPassword;
}
