package com.example.bookmanagement.usecase;

public class ResponseError {
    private String errorMessage;
    private String errorCode;

    public ResponseError(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
