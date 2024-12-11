package com.lambdacode.creditscoreanalysis.Service;

import com.lambdacode.creditscoreanalysis.Request.LoginRequest;

public interface LoginService {
    String verify(LoginRequest loginRequest);
}
