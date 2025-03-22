package com.lambdacode.creditscoreanalysiswebapplication.Response;

public class JwtResponse {
    private Integer code;
    private String message;
    private Boolean status;
    private Object data;

    // Getter and Setter for code
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    // Getter and Setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and Setter for status
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    // Getter and Setter for data
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
