package com.example.bookmanagement.usecaseEdit;

public class ResponseErrorEdit extends Exception {
    private String message;
    
    public ResponseErrorEdit(String message) {
        super(message);
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
