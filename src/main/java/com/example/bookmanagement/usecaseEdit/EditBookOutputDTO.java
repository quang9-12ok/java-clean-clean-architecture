package com.example.bookmanagement.usecaseEdit;

public class EditBookOutputDTO {
    private String maSach; 
    private String loaiSach; 
    private String nhaXuatBan; 
    private double donGia; 
    private int soLuong;
    private String tinhTrang;
    private double thue; 

    // Constructor
    public EditBookOutputDTO(String maSach, String loaiSach, String nhaXuatBan, double donGia, int soLuong, String tinhTrang, double thue) {
        this.maSach = maSach;
        this.loaiSach = loaiSach;
        this.nhaXuatBan = nhaXuatBan;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.tinhTrang = tinhTrang;
        this.thue = thue;
    }

    // Getters
    public String getMaSach() {
        return maSach;
    }

    public String getLoaiSach() {
        return loaiSach;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public double getDonGia() {
        return donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public double getThue() {
        return thue;
    }

  
    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public void setLoaiSach(String loaiSach) {
        this.loaiSach = loaiSach;
    }

    public void setNhaXuatBan(String nhaXuatBan) {
        this.nhaXuatBan = nhaXuatBan;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public void setThue(double thue) {
        this.thue = thue;
    }
}