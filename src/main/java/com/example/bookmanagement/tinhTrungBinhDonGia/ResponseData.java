package com.example.bookmanagement.tinhTrungBinhDonGia;

public interface ResponseData {
    boolean isSuccess();
    String getMessage();
    double getAveragePrice();
    int getTotalBooks();
}
