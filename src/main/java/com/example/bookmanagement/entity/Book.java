package com.example.bookmanagement.entity;

import java.util.Date;

public abstract class Book {
    protected int maSach;
    protected Date ngayNhap;
    protected double donGia;
    protected int soLuong;
    protected String nhaXuatBan;
    protected String loaiSach;

    // Constructor
    public Book(int maSach, Date ngayNhap, double donGia, int soLuong, String nhaXuatBan, String loaiSach) {
        this.maSach = maSach;
        this.ngayNhap = ngayNhap;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.nhaXuatBan = nhaXuatBan;
        this.loaiSach = loaiSach;
    }

  
    public abstract String getDetail();
    public abstract double tinhThanhTien();
    
 
    public boolean isValid() {
        return maSach > 0 && 
               donGia > 0 && 
               soLuong >= 0 && 
               nhaXuatBan != null && 
               !nhaXuatBan.trim().isEmpty() &&
               loaiSach != null && 
               !loaiSach.trim().isEmpty();
    }

    public String getSearchInfo() {
        return String.format("Mã sách: %d, NXB: %s, Loại: %s", 
            maSach, nhaXuatBan, loaiSach);
    }

    // Getters and Setters
    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public void setNhaXuatBan(String nhaXuatBan) {
        this.nhaXuatBan = nhaXuatBan;
    }

    public String getLoaiSach() {
        return loaiSach;
    }

    public void setLoaiSach(String loaiSach) {
        this.loaiSach = loaiSach;
    }
    
    
}