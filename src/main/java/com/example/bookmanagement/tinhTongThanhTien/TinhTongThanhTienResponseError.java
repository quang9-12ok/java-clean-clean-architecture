package com.example.bookmanagement.tinhTongThanhTien;

public class TinhTongThanhTienResponseError {
    public static final String ERROR_DANH_SACH_TRONG = "Danh sách sách trống.";
    private String errorMessage;

    private TinhTongThanhTienResponseError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static TinhTongThanhTienResponseError databaseError() {
        return new TinhTongThanhTienResponseError("Database error occurred.");
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
