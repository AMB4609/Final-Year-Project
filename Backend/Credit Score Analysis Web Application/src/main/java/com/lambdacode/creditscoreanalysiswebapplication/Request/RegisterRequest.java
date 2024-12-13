package com.lambdacode.creditscoreanalysiswebapplication.Request;


import lombok.Data;

@Data
public class RegisterRequest {
    private Integer userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userSurname;
    private Integer userPhone;
}
