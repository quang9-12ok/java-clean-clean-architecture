package com.example.bookmanagement.usecaseEdit;

public interface ResponseDataEdit {
    int getMaSach();
    String getLoaiSach();
    String getTinhTrang();
    Double getThue();
    
    void setMaSach(int maSach);
    void setLoaiSach(String loaiSach);
    void setTinhTrang(String tinhTrang); 
    void setThue(Double thue);
}
