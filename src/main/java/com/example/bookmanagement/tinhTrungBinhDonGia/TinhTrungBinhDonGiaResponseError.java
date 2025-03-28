package com.example.bookmanagement.tinhTrungBinhDonGia;

public class TinhTrungBinhDonGiaResponseError {
    public static final String ERROR_DANH_SACH_TRONG = "Danh sách sách tham khảo trống.";
    public static final String ERROR_DATABASE = "Lỗi kết nối cơ sở dữ liệu.";

    public static TinhTrungBinhDonGiaResponseError databaseError() {
        return new TinhTrungBinhDonGiaResponseError();
    }
    
    public String getErrorMessage() {
        return ERROR_DATABASE;
    }
}
