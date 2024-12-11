package com.lambdacode.creditscoreanalysis.Request;


import lombok.Data;

@Data
public class RegisterRequest {
    private Integer userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private String userAddress;
}
