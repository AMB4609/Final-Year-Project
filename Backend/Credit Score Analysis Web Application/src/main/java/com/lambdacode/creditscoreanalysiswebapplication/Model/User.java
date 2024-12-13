package com.lambdacode.creditscoreanalysiswebapplication.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "User")
public class User {
    @Id
    @GeneratedValue
    private Integer userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userSurname;
    private Integer userPhone;
}
