package com.example.bookmanagement.timSach;

import java.util.Date;

public interface TimSachInputBoundary {
    void timSachTheoMa(Integer maSach);
    void timSachTheoNgayNhap(Date ngayNhap);
    void timSachTheoNhaXuatBan(String nhaXuatBan);
    void timSachTheoLoai(String loaiSach);
    void timSachTheoDonGia(Double donGia);
}