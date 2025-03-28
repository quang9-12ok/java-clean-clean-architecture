package com.example.bookmanagement.entity;

import java.util.Date;

public class TextBook extends Book {
    private String tinhTrang; // tình trạng sách (mới/cũ)
    
    // Constructor
    public TextBook(int maSach, Date ngayNhap, double donGia, int soLuong, String nhaXuatBan, String loaiSach, String tinhTrang) {
        super(maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach);
        this.tinhTrang = tinhTrang;

        System.out.println("DEBUG: TextBook constructed - Tình trạng: " + this.tinhTrang);
    }

    // Getter và Setter
    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public String getDetail() {
        return String.format("Sách giáo khoa - Mã sách: %d\n" +
                "Ngày nhập: %s\n" +
                "Đơn giá: %.2f\n" +
                "Số lượng: %d\n" +
                "Nhà xuất bản: %s\n" +
                "Loại sách: %s\n" +
                "Tình trạng: %s",
                maSach, ngayNhap, donGia, soLuong, nhaXuatBan, loaiSach, tinhTrang);
    }

    @Override
public double tinhThanhTien() {
    double thanhTien = donGia * soLuong;
    if ("Cũ".equalsIgnoreCase(tinhTrang)) {
        thanhTien *= 0.5; 
    }
    return thanhTien;
}

    @Override
    public boolean isValid() {
        return super.isValid() && 
               tinhTrang != null && 
               !tinhTrang.trim().isEmpty() &&
               (tinhTrang.equalsIgnoreCase("Mới") || tinhTrang.equalsIgnoreCase("Cũ"));
    }

    public String getPublisherInfo() {
        return String.format("Sách giáo khoa - NXB: %s, Tình trạng: %s", 
            nhaXuatBan, tinhTrang);
    }
}