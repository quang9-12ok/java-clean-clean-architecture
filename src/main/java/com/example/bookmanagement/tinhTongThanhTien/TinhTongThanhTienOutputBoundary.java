package com.example.bookmanagement.tinhTongThanhTien;

public interface TinhTongThanhTienOutputBoundary {
    void presentResult(TinhTongThanhTienOutputDTO output);
    void presentResult(String errorMessage);
}