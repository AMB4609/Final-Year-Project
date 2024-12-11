package com.lambdacode.creditscoreanalysis.Request;


import lombok.Data;

@Data
public class LoginRequest {
    private String userEmail;
    private String userPassword;
}
