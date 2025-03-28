package com.example.bookmanagement.themSach;

public class DataExport implements ResponseData {
    private boolean success;
    private String message;
    
    public DataExport(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
