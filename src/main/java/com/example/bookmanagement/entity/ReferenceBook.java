package com.example.bookmanagement.entity;

import java.util.Date;

public class ReferenceBook extends Book {
    private double thue; // thuế
    
    // Constructor
    public ReferenceBook(int maSach, Date ngayNhap, double donGia, int soLuong, String nhaXuatBan, String loaiSach, double thue) {
        super(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach);
        this.thue = thue;
        System.out.println("DEBUG: ReferenceBook constructed - thuế: " + this.thue);
    }

    // Getter và Setter
    public double getThue() {
        return thue;
    }

    public void setThue(double thue) {
        this.thue = thue;
    }

    @Override
    public String getDetail() {
        return String.format("Sách tham khảo - Mã sách: %d\n" +
                "Ngày nhập: %s\n" +
                "Đơn giá: %.2f\n" +
                "Số lượng: %d\n" +
                "Nhà xuất bản: %s\n" +
                "Loại sách: %s\n" +
                "Thuế: %.2f%%",
                maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach, thue);
    }

    @Override
    public double tinhThanhTien() {
        double thanhTien = donGia * soLuong;
        return thanhTien + (thanhTien * thue / 100); // Cộng thêm thuế
    }

    @Override
    public boolean isValid() {
        return super.isValid() && 
               thue >= 0 && 
               thue <= 100; // Thuế từ 0% đến 100%
    }

    public String getTaxInfo() {
        return String.format("Sách tham khảo - Thuế: %.2f%%", thue);
    }
}