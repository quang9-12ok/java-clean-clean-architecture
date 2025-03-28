package com.example.bookmanagement.timSach;

public interface ResponseData<T> {
    T getData();
    boolean isSuccess();
    String getMessage();
    boolean isRetryRequested();
}